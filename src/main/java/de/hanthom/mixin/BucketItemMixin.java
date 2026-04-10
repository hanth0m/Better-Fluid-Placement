package de.hanthom.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
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
	private Fluid content;

	@ModifyArgs(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BucketItem;emptyContents(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/BlockHitResult;)Z"))
	private void cancelWaterLogs(Args args) {
		LivingEntity user = args.get(0);
		Level world = args.get(1);
		BlockPos pos = args.get(2);
		BlockHitResult hitResult = args.get(3);
		if (world.getBlockState(pos).hasProperty(BlockStateProperties.WATERLOGGED) && this.content == Fluids.WATER) {
			if (user.isShiftKeyDown()) {
				args.set(2, pos.relative(hitResult.getDirection()));
			}
		} else if (!user.isShiftKeyDown() && (world.getBlockState(hitResult.getBlockPos()).is(BlockTags.SMALL_FLOWERS)
				|| world.getBlockState(hitResult.getBlockPos()).is(BlockTags.SAPLINGS)
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.GRASS
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.DEAD_BUSH
				|| world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.FERN
		)) {
			args.set(2, hitResult.getBlockPos());
		}
	}
}