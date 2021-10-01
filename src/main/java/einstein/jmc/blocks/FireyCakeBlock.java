package einstein.jmc.blocks;

import einstein.jmc.init.ModServerConfigs;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class FireyCakeBlock extends BaseCakeBlock
{
    public FireyCakeBlock(final BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    @Override
    public void eatActions(Player player) {
		player.getFoodData().eat(2, 0.1F);
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, ModServerConfigs.FIREY_CAKE_FIRE_RES_DUR.get(), ModServerConfigs.FIREY_CAKE_FIRE_RES_STRENGTH.get()));
        player.setSecondsOnFire(ModServerConfigs.FIREY_CAKE_ON_FIRE_DUR.get());
    }
}
