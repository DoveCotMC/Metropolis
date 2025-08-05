package team.dovecotmc.metropolis.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.block.entity.BlockEntityFareAdj;
import team.dovecotmc.metropolis.block.entity.BlockEntityTicketVendor;
import team.dovecotmc.metropolis.block.entity.MetroBlockEntities;
import team.dovecotmc.metropolis.item.MetroItems;
import team.dovecotmc.metropolis.network.MetroServerNetwork;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockFareAdjMachine extends HorizontalDirectionalBlock implements EntityBlock {
    public final Block defaultUpper;

    public BlockFareAdjMachine() {
        this(MetroBlocks.BLOCK_TICKET_VENDOR_UP_1);
    }

    public BlockFareAdjMachine(Block defaultUpper) {
        super(Properties.of(Material.METAL).strength(6.0f).noOcclusion().lightLevel(value -> 0));
        this.defaultUpper = defaultUpper;
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return MetroBlockUtil.getVoxelShapeByDirection(0.0, 0.0, 4.0, 16.0 , 16.0, 16.0, facing);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);
        if (world.getBlockState(pos.above()).isAir()) {
            world.setBlockAndUpdate(pos.above(), defaultUpper.defaultBlockState().setValue(FACING, state.getValue(FACING)));
        }
    }

    @Override
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
        super.destroy(world, pos, state);
        if (world.getBlockState(pos.above()).getBlock() instanceof BlockTicketVendorUp) {
            world.destroyBlock(pos.above(), false);
        }
    }

    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!world.isClientSide) {
            BlockEntityFareAdj blockEntity = world.getBlockEntity(pos, MetroBlockEntities.FARE_ADJ_BLOCK_ENTITY).orElse(null);

            if (blockEntity != null && !blockEntity.getItem(0).isEmpty()) {
                world.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 1f, 1f);
                ServerPlayer serverPlayer = (ServerPlayer) player;
                serverPlayer.getInventory().add(blockEntity.getItem(0));
                blockEntity.removeItemNoUpdate(0);
                serverPlayer.connection.send(blockEntity.getUpdatePacket());
                MetroServerNetwork.removeInventoryItem(0, pos, serverPlayer);
            } else if (blockEntity != null && player.getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(MetroItems.ITEM_CARD)) {
                world.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1f, 1f);
                ServerPlayer serverPlayer = (ServerPlayer) player;

                serverPlayer.connection.send(blockEntity.getUpdatePacket());
                MetroServerNetwork.openFareAdjustmentScreen(pos, (ServerPlayer) player);
            } else {
                if (blockEntity != null) {
                    blockEntity.removeItemNoUpdate(1);
                    MetroServerNetwork.removeInventoryItem(1, pos, (ServerPlayer) player);
                    MetroServerNetwork.openFareAdjustmentScreen(pos, (ServerPlayer) player);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityFareAdj(pos, state);
    }
}
