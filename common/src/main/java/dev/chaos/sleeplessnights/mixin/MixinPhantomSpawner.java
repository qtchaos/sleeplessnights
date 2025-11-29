package dev.chaos.sleeplessnights.mixin;

import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomSpawner.class)
public class MixinPhantomSpawner {
    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tick(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies, CallbackInfoReturnable<Integer> cir) {
        RandomSource random = level.random;
        // 4/5 chance to skip spawning completely
        if (random.nextInt(5) != 0) {
            cir.setReturnValue(0);
        }
    }
}
