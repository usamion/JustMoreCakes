package einstein.jmc.init;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.menu.cakeoven.CakeOvenMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {

	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, JustMoreCakes.MODID);
	
	public static final RegistryObject<MenuType<CakeOvenMenu>> CAKE_OVEN = MENU_TYPES.register("cake_oven", () -> IForgeMenuType.create((id, inventory, data) -> new CakeOvenMenu(id, inventory)));
}
