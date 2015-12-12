package mod.wireframe;

import java.util.Map;

import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.FMLRelaunchLog;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class WireFrame extends DummyModContainer implements IFMLLoadingPlugin
{

	protected final ModMetadata md = new ModMetadata();

	@EventHandler
	public void load(
			final FMLInitializationEvent event )
	{
	}

	public WireFrame()
	{
		FMLRelaunchLog.info( "[WireFrame] Init" );
		md.autogenerated = true;
		md.credits = "AlgorithmX2";
		md.modId = getModId();
		md.version = getVersion();
		md.name = getName();
	}

	@Override
	public boolean registerBus(
			final EventBus bus,
			final LoadController controller )
	{
		return true;
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return null;
	}

	@Override
	public String getModContainerClass()
	{
		return "mod.wireframe.WireFrame";
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public String getModId()
	{
		return "WireFrame";
	}

	@Override
	public String getName()
	{
		return "WireFrame";
	}

	@Override
	public String getVersion()
	{
		return "1.0.0.0";
	}

	@Override
	public String getDisplayVersion()
	{
		return getVersion();
	}

	@Override
	public ModMetadata getMetadata()
	{
		return md;
	}

	@Override
	public String getAccessTransformerClass()
	{
		return "mod.wireframe.ASMTransformer";
	}

	@Override
	public void injectData(
			final Map<String, Object> data )
	{

	}
}
