package team.dovecotmc.metropolis.metropolis.block;

import mtr.data.RailwayData;
import mtr.data.TransportMode;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Arrokoth
 * @project DovecotRailwayDeco-1.19.2
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("deprecation")
public class BlockPCMonitor extends HorizontalFacingBlock {
    public BlockPCMonitor(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {
            RailwayData railwayData = RailwayData.getInstance(world);
            if (railwayData != null) {
                PacketTrainDataGuiServer.openDashboardScreenS2C((ServerPlayerEntity)player, TransportMode.TRAIN, railwayData.getUseTimeAndWindSync());
            }
        }

        return ActionResult.SUCCESS;
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
