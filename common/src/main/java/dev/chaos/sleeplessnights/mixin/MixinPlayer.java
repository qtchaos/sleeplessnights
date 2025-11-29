package dev.chaos.sleeplessnights.mixin;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(Player.class)
public class MixinPlayer {
    @Inject(method = "startSleepInBed", at = @At("HEAD"), cancellable = true)
    public void startSleepInBed(BlockPos bedPos, CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> cir) {
        Player player = (Player) (Object) this;
        Level level = player.level();

        List<String> indoorMessages = List.of(
                "The room is spinning too much.",
                "The ceiling is staring back at me.",
                "The bed feels uncomfortable.",
                "Is that a noise outside?",
                "The shadows seem to be moving.",
                "I can't find a comfortable spot.",
                "I feel trapped in here.",
                "You don't feel tired.",
                "Not now.",
                "Maybe I should do something else?",
                "I feel too sick to rest.",
                "My head hurts too much to sleep.",
                "I can't close my eyes right now.",
                "Something feels wrong...",
                "My stomach churns.",
                "A nightmare awaits if I close my eyes."
        );

        List<String> outdoorMessages = List.of(
                "The world is too loud.",
                "The air feels heavy here.",
                "I have to keep moving.",
                "I feel watched.",
                "Morning will never come.",
                "Just survive the night.",
                "You don't feel tired.",
                "Not now.",
                "Rest is impossible.",
                "I need to stay awake.",
                "Not while I feel like this.",
                "My thoughts are racing too fast.",
                "Dizzy... so dizzy...",
                "Just a little longer..."
        );

        List<String> currentMessages;
        if (level.canSeeSky(bedPos)) {
            currentMessages = outdoorMessages;
        } else {
            currentMessages = indoorMessages;
        }

        Random rand = new Random();
        String randomMessage = currentMessages.get(rand.nextInt(currentMessages.size()));

        if ((Object) this instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(
                    Component.literal(randomMessage)
            ));
            serverPlayer.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
        }

        // Block sleep
        cir.setReturnValue(Either.left(Player.BedSleepingProblem.OTHER_PROBLEM));
    }
}
