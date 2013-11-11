package uk.co.shadeddimensions.ep3.network;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.block.BlockDecoration;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.block.BlockPortal;
import uk.co.shadeddimensions.ep3.block.BlockScanner;
import uk.co.shadeddimensions.ep3.block.BlockStabilizer;
import uk.co.shadeddimensions.ep3.item.ItemDecoration;
import uk.co.shadeddimensions.ep3.item.ItemFrame;
import uk.co.shadeddimensions.ep3.item.ItemGoggles;
import uk.co.shadeddimensions.ep3.item.ItemLocationCard;
import uk.co.shadeddimensions.ep3.item.ItemPaintbrush;
import uk.co.shadeddimensions.ep3.item.ItemPortalModule;
import uk.co.shadeddimensions.ep3.item.ItemScanner;
import uk.co.shadeddimensions.ep3.item.ItemStabilizer;
import uk.co.shadeddimensions.ep3.item.ItemSynchronizer;
import uk.co.shadeddimensions.ep3.item.ItemWrench;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.packet.PacketTileUpdate;
import uk.co.shadeddimensions.ep3.portal.NetworkManager;
import uk.co.shadeddimensions.ep3.tileentity.TileEnhancedPortals;
import uk.co.shadeddimensions.ep3.tileentity.TileFrame;
import uk.co.shadeddimensions.ep3.tileentity.TilePortal;
import uk.co.shadeddimensions.ep3.tileentity.TileScanner;
import uk.co.shadeddimensions.ep3.tileentity.TileScannerFrame;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileNetworkInterface;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileRedstoneInterface;
import cofh.util.ConfigHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy
{
    public static BlockFrame blockFrame;
    public static BlockPortal blockPortal;
    public static BlockStabilizer blockStabilizer;
    public static BlockScanner blockScanner;
    public static BlockDecoration blockDecoration;

    public static ItemWrench itemWrench;
    public static ItemPaintbrush itemPaintbrush;    
    public static ItemGoggles itemGoggles;
    public static ItemPortalModule itemUpgrade;
    public static ItemLocationCard itemLocationCard;
    public static ItemSynchronizer itemSynchronizer;

    public static NetworkManager networkManager;

    public static final Logger logger = Logger.getLogger(Reference.NAME);
    public static ConfigHandler configuration;

    public static boolean showExtendedRedstoneInformation, customNetherPortals, portalsDestroyBlocks, fasterPortalCooldown, requirePower; // Bools
    public static double buildCraftPowerMultiplier, industrialCraftPowerMultiplier, thermalExpansionPowerMultiplier; // Doubles

    public static void sendUpdatePacketToPlayer(TileEnhancedPortals tile, EntityPlayer player)
    {
        if (CommonProxy.isClient())
        {
            return;
        }

        PacketDispatcher.sendPacketToPlayer(new PacketTileUpdate(tile).getPacket(), (Player) player);
    }

    public static void sendUpdatePacketToAllAround(TileEnhancedPortals tile)
    {
        if (CommonProxy.isClient())
        {
            return;
        }

        sendPacketToAllAround(tile, new PacketTileUpdate(tile).getPacket());
    }

    public static void sendPacketToAllAround(TileEntity tile, Packet250CustomPayload packet)
    {
        if (CommonProxy.isClient())
        {
            return;
        }

        PacketDispatcher.sendPacketToAllAround(tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5, 128, tile.worldObj.provider.dimensionId, packet);
    }

    public File getBaseDir()
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getFile(".");
    }

    public File getResourcePacksDir()
    {
        return new File(getBaseDir(), "resourcepacks");
    }

    public File getWorldDir()
    {
        return new File(getBaseDir(), DimensionManager.getWorld(0).getSaveHandler().getWorldDirectoryName());
    }

    public void registerBlocks()
    {
        blockFrame = new BlockFrame(configuration.getBlockId("Frame"), "ep3.portalFrame");
        GameRegistry.registerBlock(blockFrame, ItemFrame.class, "ep3.portalFrame");

        blockPortal = new BlockPortal(configuration.getBlockId("Portal"), "ep3.portal");
        GameRegistry.registerBlock(blockPortal, "ep3.portal");

        blockStabilizer = new BlockStabilizer(configuration.getBlockId("DimensionalBridgeStabilizer"), "ep3.stabilizer");
        GameRegistry.registerBlock(blockStabilizer, ItemStabilizer.class, "ep3.stabilizer");

        blockScanner = new BlockScanner(configuration.getBlockId("Scanner"), "ep3.scanner");
        GameRegistry.registerBlock(blockScanner, ItemScanner.class, "ep3.scanner");

        blockDecoration = new BlockDecoration(configuration.getBlockId("Decoration"), "ep3.decoration");
        GameRegistry.registerBlock(blockDecoration, ItemDecoration.class, "ep3.decoration");
    }

    public void registerItems()
    {
        itemWrench = new ItemWrench(configuration.getItemId("Wrench"), "ep3.wrench");
        GameRegistry.registerItem(itemWrench, "ep3.wrench");

        itemPaintbrush = new ItemPaintbrush(configuration.getItemId("Paintbrush"), "ep3.paintbrush");
        GameRegistry.registerItem(itemPaintbrush, "ep3.paintbrush");

        itemGoggles = new ItemGoggles(configuration.getItemId("Glasses"), "ep3.goggles");
        GameRegistry.registerItem(itemGoggles, "ep3.goggles");

        itemLocationCard = new ItemLocationCard(configuration.getItemId("LocationCard"), "ep3.locationCard");
        GameRegistry.registerItem(itemLocationCard, "ep3.locationCard");

        itemUpgrade = new ItemPortalModule(configuration.getItemId("PortalModule"), "ep3.portalModule");
        GameRegistry.registerItem(itemUpgrade, "ep3.portalModule");

        itemSynchronizer = new ItemSynchronizer(configuration.getItemId("Synchronizer"), "ep3.synchronizer");
        GameRegistry.registerItem(itemSynchronizer, "ep3.synchronizer");
    }

    public void registerRenderers()
    {

    }

    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TilePortal.class, "epPortal");
        GameRegistry.registerTileEntity(TileFrame.class, "epPortalFrame");
        GameRegistry.registerTileEntity(TilePortalController.class, "epPortalController");
        GameRegistry.registerTileEntity(TileRedstoneInterface.class, "epRedstoneInterface");
        GameRegistry.registerTileEntity(TileNetworkInterface.class, "epNetworkInterface");
        GameRegistry.registerTileEntity(TileDiallingDevice.class, "epDiallingDevice");
        GameRegistry.registerTileEntity(TileBiometricIdentifier.class, "epBiometricIdentifier");
        GameRegistry.registerTileEntity(TileModuleManipulator.class, "epModuleManipulator");
        GameRegistry.registerTileEntity(TileStabilizer.class, "epStabilizer");
        GameRegistry.registerTileEntity(TileScanner.class, "epScanner");
        GameRegistry.registerTileEntity(TileScannerFrame.class, "epScannerFrame");
    }

    public void setupConfiguration(Configuration theConfig)
    {
        configuration = new ConfigHandler(Reference.VERSION);
        configuration.setConfiguration(theConfig);

        configuration.addBlockEntry("Portal");
        configuration.addBlockEntry("Frame");
        configuration.addBlockEntry("DimensionalBridgeStabilizer");
        configuration.addBlockEntry("Scanner");
        configuration.addBlockEntry("Decoration");

        configuration.addItemEntry("Wrench");
        configuration.addItemEntry("Glasses");
        configuration.addItemEntry("Paintbrush");
        configuration.addItemEntry("LocationCard");
        configuration.addItemEntry("Synchronizer");
        configuration.addItemEntry("PortalModule");

        showExtendedRedstoneInformation = configuration.get("Misc", "ShowExtendedRedstoneInformation", false);
        customNetherPortals = configuration.get("Overrides", "CustomNetherPortals", false);

        portalsDestroyBlocks = configuration.get("Portal", "PortalsDestroyBlocks", true);
        fasterPortalCooldown = configuration.get("Portal", "FasterPortalCooldown", false);

        requirePower = configuration.get("Power", "RequirePower", true);
        buildCraftPowerMultiplier = configuration.get("Power", "BuildCraftPowerMultiplier", 1);
        industrialCraftPowerMultiplier = configuration.get("Power", "IndustrialCraftPowerMultiplier", 1);
        thermalExpansionPowerMultiplier = configuration.get("Power", "ThermalExpansionPowerMultiplier", 1);

        configuration.init();
    }

    ArrayList<Integer> usedBlocks = new ArrayList<Integer>();
    ArrayList<Integer> usedItems = new ArrayList<Integer>();

    boolean hasUsed(int i, int j)
    {
        if (i == 0)
        {
            return usedBlocks.contains(j);
        }
        else if (i == 1)
        {
            return usedItems.contains(j);
        }

        return false;
    }

    void setUsed(int i, int j)
    {
        if (i == 0)
        {
            usedBlocks.add(j);
        }
        else if (i == 1)
        {
            usedItems.add(j);
        }
    }

    public static boolean isClient()
    {
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }

    public static boolean isServer()
    {
        return !isClient();
    }

    public void miscSetup()
    {

    }

    public static void openGui(EntityPlayer player, GUIs gui, TileEnhancedPortals tile)
    {
        openGui(player, gui.ordinal(), tile);
    }

    public static void openGui(EntityPlayer player, int id, TileEnhancedPortals tile)
    {        
        player.openGui(EnhancedPortals.instance, id, tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord);
    }
}
