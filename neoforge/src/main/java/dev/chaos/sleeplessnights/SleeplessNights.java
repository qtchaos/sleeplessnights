package dev.chaos.sleeplessnights;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class SleeplessNights {

    public SleeplessNights(IEventBus eventBus) {
        CommonClass.init();
    }
}
