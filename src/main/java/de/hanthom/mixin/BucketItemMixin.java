package de.hanthom.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.states.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin {
	@Shadow
	@Final
	private Fluid field_17157;

	@ModifyArgs(method = "method_13649", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BucketItem;method_16028(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/hit/BlockHitResult;)Z"))
	private void cancelWaterLogs(Args args) {
		LivingEntity user = args.get(0);
		World world = args.get(1);
		BlockPos pos = args.get(2);
		BlockHitResult hitResult = args.get(3);
		if (world.getBlockState(pos).method_16933(Properties.WATERLOGGED) && this.field_17157 == Fluids.WATER) {
			if (user.isSneaking()) {
				args.set(2, pos.offset(hitResult.direction));
			}
		} else if (!user.isSneaking() && (
				world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.POPPY
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.DANDELION
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.AZURE_BLUET
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.ORANGE_TULIP
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.PINK_TULIP
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.RED_TULIP
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.WHITE_TULIP
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.BLUE_ORCHID
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.ALLIUM
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.OXEYE_DAISY
				|| world.getBlockState(hitResult.getBlockPos()).isIn(BlockTags.SAPLINGS)
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.GRASS
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.DEAD_BUSH
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.FERN
		)) {
			args.set(2, hitResult.getBlockPos());
		}
	}
}