package de.hanthom.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin implements BucketItemAccessorMixin {
	@ModifyArgs(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BucketItem;emptyContents(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/BlockHitResult;)Z"))
	private void cancelWaterLogs(Args args) {
		LivingEntity user = args.get(0);
		Level world = args.get(1);
		BlockPos pos = args.get(2);
		BlockHitResult hitResult = args.get(3);
		if (world.getBlockState(pos).hasProperty(BlockStateProperties.WATERLOGGED) && this.getFluid() == Fluids.WATER) {
			if (user.isShiftKeyDown()) {
				args.set(2, pos.relative(hitResult.getDirection()));
			}
		} else if (!user.isShiftKeyDown() && (world.getBlockState(hitResult.getBlockPos()).is(BlockTags.SMALL_FLOWERS)
				|| world.getBlockState(hitResult.getBlockPos()).is(BlockTags.SAPLINGS)
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.SHORT_GRASS || world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.BUSH
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.DEAD_BUSH || world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.LEAF_LITTER
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.FERN
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.PINK_PETALS
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.WILDFLOWERS
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.SHORT_DRY_GRASS
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.FIREFLY_BUSH
		)) {
			args.set(2, hitResult.getBlockPos());
		}
	}
}