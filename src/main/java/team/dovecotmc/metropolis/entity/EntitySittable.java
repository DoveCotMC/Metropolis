package team.dovecotmc.metropolis.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
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
    private static final ImmutableMap<EntityPose, ImmutableList<Integer>> DISMOUNT_FREE_Y_SPACES_NEEDED;
    private int tick = 0;

    public EntitySittable(EntityType<?> type, World world) {
        super(type, world);
        setInvulnerable(true);
        setInvisible(true);
        setNoGravity(true);
    }

    @Override
    public void updatePassengerPosition(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            double d = this.getY() + this.getMountedHeightOffset() + passenger.getHeightOffset();
            passenger.setPos(this.getX(), d + passenger.getEyeHeight(EntityPose.CROUCHING), this.getZ());
        }
    }

    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Direction direction = this.getHorizontalFacing().rotateYCounterclockwise();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.updatePassengerForDismount(passenger);
        } else {
            int[][] is = Dismounting.getDismountOffsets(direction);
            BlockPos blockPos = this.getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            ImmutableList<EntityPose> immutableList = passenger.getPoses();

            for (EntityPose entityPose : immutableList) {
                EntityDimensions entityDimensions = passenger.getDimensions(entityPose);
                float f = Math.min(entityDimensions.width, 1.0F) / 2.0F;

                for (Integer o : Objects.requireNonNull(DISMOUNT_FREE_Y_SPACES_NEEDED.get(entityPose))) {
                    int i = o;
                    int var14 = is.length;

                    for (int[] js : is) {
                        mutable.set(blockPos.getX() + js[0], blockPos.getY() + i, blockPos.getZ() + js[1]);
                        double d = this.world.getDismountHeight(Dismounting.getCollisionShape(this.world, mutable), () -> {
                            return Dismounting.getCollisionShape(this.world, mutable.down());
                        });
                        if (Dismounting.canDismountInBlock(d)) {
                            Box box = new Box((double) (-f), 0.0, (double) (-f), (double) f, (double) entityDimensions.height, (double) f);
                            Vec3d vec3d = Vec3d.ofCenter(mutable, d);
                            if (Dismounting.canPlaceEntityAt(this.world, passenger, box.offset(vec3d))) {
                                passenger.setPose(entityPose);
                                return vec3d;
                            }
                        }
                    }
                }
            }

            double e = this.getBoundingBox().maxY;
            mutable.set((double)blockPos.getX(), e, (double)blockPos.getZ());

            for (EntityPose entityPose2 : immutableList) {
                double g = (double) passenger.getDimensions(entityPose2).height;
                int j = MathHelper.ceil(e - (double) mutable.getY() + g);
                double h = Dismounting.getCeilingHeight(mutable, j, (pos) -> {
                    return this.world.getBlockState(pos).getCollisionShape(this.world, pos);
                });
                if (e + g <= h) {
                    passenger.setPose(entityPose2);
                    break;
                }
            }

            Vec3d vec3d = getPassengerDismountOffset((double)this.getWidth(), (double)passenger.getWidth(), this.getYaw() + (passenger.getMainArm() == Arm.RIGHT ? 90.0F : -90.0F));
            Vec3d vec3d2 = this.locateSafeDismountingPos(vec3d, passenger);
            if (vec3d2 != null) {
                return vec3d2;
            } else {
                Vec3d vec3d3 = getPassengerDismountOffset((double)this.getWidth(), (double)passenger.getWidth(), this.getYaw() + (passenger.getMainArm() == Arm.LEFT ? 90.0F : -90.0F));
                Vec3d vec3d4 = this.locateSafeDismountingPos(vec3d3, passenger);
                return vec3d4 != null ? vec3d4 : this.getPos();
            }
        }
    }

    @Nullable
    private Vec3d locateSafeDismountingPos(Vec3d offset, LivingEntity passenger) {
        double d = this.getX() + offset.x;
        double e = this.getBoundingBox().minY;
        double f = this.getZ() + offset.z;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (EntityPose entityPose : passenger.getPoses()) {
            mutable.set(d, e, f);
            double g = this.getBoundingBox().maxY + 0.75;

            while (true) {
                double h = this.world.getDismountHeight(mutable);
                if ((double) mutable.getY() + h > g) {
                    break;
                }

                if (Dismounting.canDismountInBlock(h) && h != 0) {
                    Box box = passenger.getBoundingBox(entityPose);
                    Vec3d vec3d = new Vec3d(d, (double) mutable.getY() + h, f);
                    if (Dismounting.canPlaceEntityAt(this.world, passenger, box.offset(vec3d))) {
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

    public EntitySittable(World world, double x, double y, double z) {
        this(MetroEntities.SITTABLE, world);
        this.setPos(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    public void tick() {
        if (world.isClient || age < 5) return;
        final List<Entity> passengers = getPassengerList();
        if (passengers.isEmpty()) remove(RemovalReason.DISCARDED);
        tick++;
        tick &= 15;
        if (tick != 0) return;
        for (final var p : passengers) {
            final Text msg = isPoseValid(p.getPose()) ?
                    isSeatValid(world, getBlockPos()) ? null : Text.translatable("sittable.metropolis.invaild") : Text.translatable("sittable.metropolis.wrong_pose");
            if (msg == null) continue;
            p.stopRiding();
            p.setPos(p.getX(), getBlockPos().getY(), p.getZ());
//            if (p instanceof PlayerEntity player) player.sendMessage(msg, true);
        }
        if (getPassengerList().isEmpty()) remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }

    protected void copyEntityData(Entity entity) {
        entity.setBodyYaw(this.getYaw());
        float f = MathHelper.wrapDegrees(entity.getYaw() - this.getYaw());
        float g = MathHelper.clamp(f, -105.0F, 105.0F);
        entity.prevYaw += g - f;
        entity.setYaw(entity.getYaw() + g - f);
        entity.setHeadYaw(entity.getYaw());
    }

    @Override
    public void onPassengerLookAround(Entity passenger) {
//        super.onPassengerLookAround(passenger);
        copyEntityData(passenger);
    }

    @Override
    public double getMountedHeightOffset() {
        return getStandingEyeHeight() - .25;
    }

//    @Override
//    protected float getEyeHeight(EntityPose pose, EntityDimensions size) {
//        return size.height;
//    }

    private static final List<EntityPose> availablePoses = ImmutableList.of(EntityPose.STANDING, EntityPose.CROUCHING);
    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    public static boolean isSeatValid(World world, BlockPos pos) {
        return !world.getBlockState(pos).getMaterial().equals(Material.AIR);
    }

    public static boolean isSeatValid(World world, Vec3d vec) {
        return isSeatValid(world, new BlockPos(Math.floor(vec.x), Math.floor(vec.y - .03), Math.floor(vec.z)));
    }

    public static boolean isPoseValid(EntityPose pose) {
        return availablePoses.contains(pose);
    }

    public static boolean isOccupied(World world, BlockPos pos, Vec3d center) {
        final Vec3d real = center.add(pos.getX(), pos.getY(), pos.getZ());
        return !world.getEntitiesByClass(EntitySittable.class, new Box(real.add(-.1, -.1, -.1), real.add(.1, .1, .1)), EntityPredicates.VALID_ENTITY).isEmpty();
    }

    public static boolean trySit(World world, BlockPos pos, BlockState state, @Nullable BlockHitResult hit, PlayerEntity player) {
        final SittableRegistry registry = SittableRegistries.getSittables().get(state.getBlock());
        if (registry == null) return false;
        final Optional<Vec3d> o = registry.offset().get(state, player, Optional.ofNullable(hit));
        if (o.isEmpty()) return false;
        final Vec3d vec = o.get();
        if (isOccupied(world, pos, vec)) {
            player.sendMessage(Text.translatable("sittable.metropolis.info.occupied"), true);
            return true;
        }
        final EntitySittable sittable = new EntitySittable(world, pos.getX() + vec.x, pos.getY() + vec.y, pos.getZ() + vec.z);
        if (state.contains(HorizontalFacingBlock.FACING)) {
            sittable.setYaw(state.get(HorizontalFacingBlock.FACING).asRotation());
        }
        world.spawnEntity(sittable);
        if (player.hasVehicle()) player.stopRiding();
        player.startRiding(sittable);
        return true;
    }

    static {
        DISMOUNT_FREE_Y_SPACES_NEEDED = ImmutableMap.of(EntityPose.STANDING, ImmutableList.of(0, 1, -1), EntityPose.CROUCHING, ImmutableList.of(0, 1, -1), EntityPose.SWIMMING, ImmutableList.of(0, 1));
    }
}
