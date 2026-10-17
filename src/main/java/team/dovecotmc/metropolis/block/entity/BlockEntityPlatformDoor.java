package team.dovecotmc.metropolis.block.entity;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BlockEntityPlatformDoor extends BlockEntity {
    public static final int ANIMATION_TIME = 20;
    private static final boolean MTR_LOADED = FabricLoader.getInstance().isModLoaded("mtr");
    private static @Nullable Consumer<BlockEntityPlatformDoor> clientStateInitializer;

    private @Nullable BlockPos controllerPos = null;
    private long lastToggleTime = 0;
    private @Nullable Float externalOpenValue = null;
    private boolean automaticOpeningEnabled = MTR_LOADED;
    private boolean manualOpenState = false;
    private boolean clientStateNeedsRefresh = true;

    public BlockEntityPlatformDoor(BlockPos blockPos, BlockState blockState) {
        super(MetroBlockEntities.PLATFORM_DOOR_BLOCK_ENTITY, blockPos, blockState);
    }

    public float getOpenValue(Level level, float f) {
        if (controllerPos == null)
            return 1f;

        return Mth.clamp((level.getGameTime() - lastToggleTime + f) / ANIMATION_TIME, 0f, 1f);
    }

    public void setExternalOpenValue(float openValue) {
        externalOpenValue = Mth.clamp(openValue, 0f, 1f);
    }

    public static void setClientStateInitializer(Consumer<BlockEntityPlatformDoor> initializer) {
        clientStateInitializer = initializer;
    }

    public void clientTick() {
        if (clientStateNeedsRefresh && clientStateInitializer != null) {
            clientStateNeedsRefresh = false;
            clientStateInitializer.accept(this);
        }
    }

    public float getCurrentOpenValue(Level level, float partialTick) {
        if (isAutomaticOpeningEnabled()) {
            return externalOpenValue == null ? 0f : externalOpenValue;
        }
        float progress = getOpenValue(level, partialTick);
        return manualOpenState ? progress : 1f - progress;
    }

    public boolean isOpen() {
        return isAutomaticOpeningEnabled()
                ? externalOpenValue != null && externalOpenValue > 0f
                : manualOpenState;
    }

    public @Nullable Float getExternalOpenValue() {
        return isAutomaticOpeningEnabled() ? externalOpenValue : null;
    }

    public boolean isAutomaticOpeningEnabled() {
        return MTR_LOADED && automaticOpeningEnabled;
    }

    public boolean getManualOpenState() {
        return manualOpenState;
    }

    public void setManualOpenState(boolean openState) {
        if (manualOpenState != openState) {
            manualOpenState = openState;
            setChanged();
        }
    }

    public void setAutomaticOpeningEnabled(boolean enabled) {
        if (automaticOpeningEnabled != enabled) {
            automaticOpeningEnabled = enabled;
            setChanged();
        }
    }

    public void setBindingBlock(BlockPos blockPos) {
        controllerPos = blockPos;
    }

    public void setLastToggleTime(long time) {
        lastToggleTime = time;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);

        if (compoundTag.contains("controller_pos", CompoundTag.TAG_LONG))
            controllerPos = BlockPos.of(compoundTag.getLong("controller_pos"));

        lastToggleTime = compoundTag.getLong("last_toggle_time");
        automaticOpeningEnabled = compoundTag.contains("automatic_opening_enabled")
                ? compoundTag.getBoolean("automatic_opening_enabled")
                : MTR_LOADED;
        manualOpenState = compoundTag.getBoolean("manual_open_state");
        clientStateNeedsRefresh = true;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);

        if (controllerPos != null)
            compoundTag.putLong("controller_pos", controllerPos.asLong());

        compoundTag.putLong("last_toggle_time", lastToggleTime);
        compoundTag.putBoolean("automatic_opening_enabled", automaticOpeningEnabled);
        compoundTag.putBoolean("manual_open_state", manualOpenState);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
}
