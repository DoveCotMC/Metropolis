package team.dovecotmc.metropolis.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;
import team.dovecotmc.metropolis.sittable.SittableRegistries;
import team.dovecotmc.metropolis.sittable.SittableRegistry;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class EntitySittable extends Entity {
    private static final ImmutableMap<Pose, ImmutableList<Integer>> DISMOUNT_FREE_Y_SPACES_NEEDED;
    private int tick = 0;

    public EntitySittable(EntityType<?> type, Level world) {
        super(type, world);
        setInvulnerable(true);
        setInvisible(true);
        setNoGravity(true);
    }

    @Override
    public void positionRider(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            double d = this.getY() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset();
            passenger.setPosRaw(this.getX(), d + passenger.getEyeHeight(Pose.CROUCHING), this.getZ());
        }
    }

    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        Direction direction = this.getDirection().getCounterClockWise();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.getDismountLocationForPassenger(passenger);
        } else {
            int[][] is = DismountHelper.offsetsForDirection(direction);
            BlockPos blockPos = this.blockPosition();
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            ImmutableList<Pose> immutableList = passenger.getDismountPoses();

            for (Pose entityPose : immutableList) {
                EntityDimensions entityDimensions = passenger.getDimensions(entityPose);
                float f = Math.min(entityDimensions.width, 1.0F) / 2.0F;

                for (Integer o : Objects.requireNonNull(DISMOUNT_FREE_Y_SPACES_NEEDED.get(entityPose))) {
                    int i = o;
                    int var14 = is.length;

                    for (int[] js : is) {
                        mutable.set(blockPos.getX() + js[0], blockPos.getY() + i, blockPos.getZ() + js[1]);
                        double d = this.level.getBlockFloorHeight(DismountHelper.nonClimbableShape(this.level, mutable), () -> {
                            return DismountHelper.nonClimbableShape(this.level, mutable.below());
                        });
                        if (DismountHelper.isBlockFloorValid(d)) {
                            AABB box = new AABB((double) (-f), 0.0, (double) (-f), (double) f, (double) entityDimensions.height, (double) f);
                            Vec3 vec3d = Vec3.upFromBottomCenterOf(mutable, d);
                            if (DismountHelper.canDismountTo(this.level, passenger, box.move(vec3d))) {
                                passenger.setPose(entityPose);
                                return vec3d;
                            }
                        }
                    }
                }
            }

            double e = this.getBoundingBox().maxY;
            mutable.set((double)blockPos.getX(), e, (double)blockPos.getZ());

            for (Pose entityPose2 : immutableList) {
                double g = (double) passenger.getDimensions(entityPose2).height;
                int j = Mth.ceil(e - (double) mutable.getY() + g);
                double h = DismountHelper.findCeilingFrom(mutable, j, (pos) -> {
                    return this.level.getBlockState(pos).getCollisionShape(this.level, pos);
                });
                if (e + g <= h) {
                    passenger.setPose(entityPose2);
                    break;
                }
            }

            Vec3 vec3d = getCollisionHorizontalEscapeVector((double)this.getBbWidth(), (double)passenger.getBbWidth(), this.getYRot() + (passenger.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F));
            Vec3 vec3d2 = this.locateSafeDismountingPos(vec3d, passenger);
            if (vec3d2 != null) {
                return vec3d2;
            } else {
                Vec3 vec3d3 = getCollisionHorizontalEscapeVector((double)this.getBbWidth(), (double)passenger.getBbWidth(), this.getYRot() + (passenger.getMainArm() == HumanoidArm.LEFT ? 90.0F : -90.0F));
                Vec3 vec3d4 = this.locateSafeDismountingPos(vec3d3, passenger);
                return vec3d4 != null ? vec3d4 : this.position();
            }
        }
    }

    @Nullable
    private Vec3 locateSafeDismountingPos(Vec3 offset, LivingEntity passenger) {
        double d = this.getX() + offset.x;
        double e = this.getBoundingBox().minY;
        double f = this.getZ() + offset.z;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (Pose entityPose : passenger.getDismountPoses()) {
            mutable.set(d, e, f);
            double g = this.getBoundingBox().maxY + 0.75;

            while (true) {
                double h = this.level.getBlockFloorHeight(mutable);
                if ((double) mutable.getY() + h > g) {
                    break;
                }

                if (DismountHelper.isBlockFloorValid(h) && h != 0) {
                    AABB box = passenger.getLocalBoundsForPose(entityPose);
                    Vec3 vec3d = new Vec3(d, (double) mutable.getY() + h, f);
                    if (DismountHelper.canDismountTo(this.level, passenger, box.move(vec3d))) {
                        passenger.setPose(entityPose);
                        return vec3d;
                    }
                }

                mutable.move(Direction.UP);
                if (!((double) mutable.getY() < g)) {
                    break;
                }
            }
        }

        return null;
    }

    public EntitySittable(Level world, double x, double y, double z) {
        this(MetroEntities.SITTABLE, world);
        this.setPosRaw(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        if (level.isClientSide || tickCount < 5) return;
        final List<Entity> passengers = getPassengers();
        if (passengers.isEmpty()) remove(RemovalReason.DISCARDED);
        tick++;
        tick &= 15;
        if (tick != 0) return;
        for (final var p : passengers) {
            final Component msg = isPoseValid(p.getPose()) ?
                    isSeatValid(level, blockPosition()) ? null : MALocalizationUtil.translatableText("sittable.metropolis.invaild") : MALocalizationUtil.translatableText("sittable.metropolis.wrong_pose");
            if (msg == null) continue;
            p.stopRiding();
            p.setPosRaw(p.getX(), blockPosition().getY(), p.getZ());
//            if (p instanceof PlayerEntity player) player.sendMessage(msg, true);
        }
        if (getPassengers().isEmpty()) remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
    }

    protected void copyEntityData(Entity entity) {
        entity.setYBodyRot(this.getYRot());
        float f = Mth.wrapDegrees(entity.getYRot() - this.getYRot());
        float g = Mth.clamp(f, -105.0F, 105.0F);
        entity.yRotO += g - f;
        entity.setYRot(entity.getYRot() + g - f);
        entity.setYHeadRot(entity.getYRot());
    }

    @Override
    public void onPassengerTurned(Entity passenger) {
//        super.onPassengerLookAround(passenger);
        copyEntityData(passenger);
    }

    @Override
    public double getPassengersRidingOffset() {
        return getEyeHeight() - .25;
    }

//    @Override
//    protected float getEyeHeight(EntityPose pose, EntityDimensions size) {
//        return size.height;
//    }

    private static final List<Pose> availablePoses = ImmutableList.of(Pose.STANDING, Pose.CROUCHING);
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    public static boolean isSeatValid(Level world, BlockPos pos) {
        return !world.getBlockState(pos).getMaterial().equals(Material.AIR);
    }

    public static boolean isSeatValid(Level world, Vec3 vec) {
        return isSeatValid(world, new BlockPos(Math.floor(vec.x), Math.floor(vec.y - .03), Math.floor(vec.z)));
    }

    public static boolean isPoseValid(Pose pose) {
        return availablePoses.contains(pose);
    }

    public static boolean isOccupied(Level world, BlockPos pos, Vec3 center) {
        final Vec3 real = center.add(pos.getX(), pos.getY(), pos.getZ());
        return !world.getEntitiesOfClass(EntitySittable.class, new AABB(real.add(-.1, -.1, -.1), real.add(.1, .1, .1)), EntitySelector.ENTITY_STILL_ALIVE).isEmpty();
    }

    public static boolean trySit(Level world, BlockPos pos, BlockState state, @Nullable BlockHitResult hit, Player player) {
        final SittableRegistry registry = SittableRegistries.getSittables().get(state.getBlock());
        if (registry == null) return false;
        final Optional<Vec3> o = registry.offset().get(state, player, Optional.ofNullable(hit));
        if (o.isEmpty()) return false;
        final Vec3 vec = o.get();
        if (isOccupied(world, pos, vec)) {
            player.displayClientMessage(MALocalizationUtil.translatableText("sittable.metropolis.info.occupied"), true);
            return true;
        }
        final EntitySittable sittable = new EntitySittable(world, pos.getX() + vec.x, pos.getY() + vec.y, pos.getZ() + vec.z);
        if (state.hasProperty(HorizontalDirectionalBlock.FACING)) {
            sittable.setYRot(state.getValue(HorizontalDirectionalBlock.FACING).toYRot());
        }
        world.addFreshEntity(sittable);
        if (player.isPassenger()) player.stopRiding();
        player.setPosRaw(sittable.position().x, sittable.position().y, sittable.position().z);
        player.startRiding(sittable);
        return true;
    }

    static {
        DISMOUNT_FREE_Y_SPACES_NEEDED = ImmutableMap.of(Pose.STANDING, ImmutableList.of(0, 1, -1), Pose.CROUCHING, ImmutableList.of(0, 1, -1), Pose.SWIMMING, ImmutableList.of(0, 1));
    }
}
