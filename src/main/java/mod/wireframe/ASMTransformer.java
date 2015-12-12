package mod.wireframe;

import org.lwjgl.opengl.GL11;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

public class ASMTransformer implements IClassTransformer
{
	private void renderChunkLayer(
			final MethodNode mn,
			final ClassNode classNode )
	{
		final boolean signatureMatch = mn.desc.equals( "(Ladf;)V" )
				|| mn.desc.equals( "(Lnet/minecraft/util/EnumWorldBlockLayer;)V" );
		final boolean nameMatch = mn.name.equals( "renderChunkLayer" ) || mn.name.equals( "a" ) || mn.name.equals( "func_178001_a" );

		if ( nameMatch && signatureMatch )
		{
			AbstractInsnNode node = mn.instructions.getFirst();
			while ( node != null )
			{
				if ( renderChunkLayer_alter( mn, node ) )
				{
					return;
				}

				node = node.getNext();
			}
		}
	}

	// want to turn it off?
	public static boolean useWire()
	{
		return true;
	}

	public static void wireOn()
	{
		if ( useWire() )
		{
			GL11.glPolygonMode( GL11.GL_FRONT_AND_BACK, GL11.GL_LINE );
		}
	}

	public static void wireOff()
	{
		GL11.glPolygonMode( GL11.GL_FRONT_AND_BACK, GL11.GL_FILL );
	}

	private boolean renderChunkLayer_alter(
			final MethodNode method,
			final AbstractInsnNode node )
	{
		if ( node instanceof MethodInsnNode )
		{
			final MethodInsnNode mn = (MethodInsnNode) node;

			if ( mn.name.equals( "pushMatrix" ) )
			{
				final InsnList list = new InsnList();

				final MethodInsnNode newMethodCall = new MethodInsnNode( Opcodes.INVOKESTATIC, this.getClass().getName().replace( '.', '/' ), "wireOn", "()V", false );
				list.add( newMethodCall );

				method.instructions.insert( mn, list );
			}

			if ( mn.name.equals( "popMatrix" ) )
			{
				final InsnList list = new InsnList();

				final MethodInsnNode newMethodCall = new MethodInsnNode( Opcodes.INVOKESTATIC, this.getClass().getName().replace( '.', '/' ), "wireOff", "()V", false );
				list.add( newMethodCall );

				method.instructions.insertBefore( mn, list );
			}
		}
		return false;
	}

	@Override
	public byte[] transform(
			final String name,
			final String transformedName,
			final byte[] basicClass )
	{
		try
		{
			if ( transformedName != null && ( transformedName.equals( "net.minecraft.client.renderer.VboRenderList" ) || transformedName.equals( "net.minecraft.client.renderer.RenderList" ) ) )
			{
				if ( !deobfuscatedEnvironment() )
				{
					return basicClass;
				}

				final ClassNode classNode = new ClassNode();
				final ClassReader classReader = new ClassReader( basicClass );
				classReader.accept( classNode, 0 );

				for ( final MethodNode mn : classNode.methods )
				{
					renderChunkLayer( mn, classNode );
				}

				final ClassWriter writer = new ClassWriter( ClassWriter.COMPUTE_MAXS );
				classNode.accept( writer );
				return writer.toByteArray();
			}
		}
		catch ( final Throwable t )
		{
			t.printStackTrace();
		}

		return basicClass;
	}

	public boolean deobfuscatedEnvironment()
	{
		final Object deObf = Launch.blackboard.get( "fml.deobfuscatedEnvironment" );
		return Boolean.valueOf( String.valueOf( deObf ) );
	}

}
