package uk.co.shadeddimensions.enhancedportals;

import java.util.Random;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import uk.co.shadeddimensions.enhancedportals.creativetab.CreativeTabEP3;
import uk.co.shadeddimensions.enhancedportals.lib.Reference;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.network.GuiHandler;
import uk.co.shadeddimensions.enhancedportals.network.PacketHandler;
import uk.co.shadeddimensions.enhancedportals.portal.NetworkManager;
import uk.co.shadeddimensions.enhancedportals.util.ConfigurationManager;
import uk.co.shadeddimensions.enhancedportals.util.CustomIconManager;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(name = Reference.NAME, modid = Reference.ID, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES, acceptedMinecraftVersions = Reference.MC_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class, channels = { Reference.ID })
public class EnhancedPortals
{
    public static ConfigurationManager config;
    public static CustomIconManager customPortalIcons, customPortalFrameIcons;

    @Instance(Reference.ID)
    public static EnhancedPortals instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.COMMON_PROXY)
    public static CommonProxy proxy;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        customPortalIcons = new CustomIconManager();
        customPortalFrameIcons = new CustomIconManager();

        CommonProxy.logger.setParent(FMLLog.getLogger());
        proxy.setupConfiguration();
        proxy.registerBlocks();
        proxy.registerTileEntities();
        proxy.registerItems();
        proxy.registerRenderers();

        MinecraftForge.EVENT_BUS.register(this);
        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());        
        CreativeTabEP3.portalColour = new Random().nextInt(15) + 1;
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        config = new ConfigurationManager(new Configuration(event.getSuggestedConfigurationFile()));
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        CommonProxy.networkManager = new NetworkManager(event);
    }
    
    @ForgeSubscribe
    public void worldSave(WorldEvent.Save event)
    {
        if (!event.world.isRemote)
        {
            CommonProxy.networkManager.saveAllData();
        }
    }
}
