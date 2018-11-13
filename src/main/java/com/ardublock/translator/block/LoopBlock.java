package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.renderable.RenderableBlock;


public class LoopBlock extends TranslatorBlock
{
	public LoopBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		String ret;
		ret = "void loop()\n{\n";
		if (translator.isGuinoProgram())
		{
			ret += "GUINO_GERER_INTERFACE();\n";
		}
		TranslatorBlock translatorBlock = getTranslatorBlockAtSocket(0);
		while (translatorBlock != null)
		{
			ret = ret + translatorBlock.toCode();
			translatorBlock = translatorBlock.nextTranslatorBlock();
			
		}
		if (translator.isScoopProgram())
		{
			ret += "yield();\n";
		}
		
		for (RenderableBlock b : translator.getLoopCodeHooks()) {
			Block guinoBlock = b.getBlock();			
			ret += translator.translate(guinoBlock.getBlockID());
		}
		
		ret = ret + "}\n\n";
		return ret;
	}
}
