package dependenceAnalysis;

import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.List;

/**
 * Created by neilwalkinshaw on 21/10/2016.
 */
public interface Slicer {


    boolean isDataDepence(AbstractInsnNode a, AbstractInsnNode b);


    boolean isControlDependentUpon(AbstractInsnNode a, AbstractInsnNode b);


    List<AbstractInsnNode> backwardSlice(AbstractInsnNode criterion);

}
