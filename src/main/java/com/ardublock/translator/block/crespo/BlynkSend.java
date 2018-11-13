
package com.ardublock.translator.block.crespo;

import java.util.ResourceBundle;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class BlynkSend extends TranslatorBlock
{
	private static ResourceBundle uiMessageBundle = ResourceBundle.getBundle("com/ardublock/block/ardublock");

	public BlynkSend(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{

		TranslatorBlock translatorBlock = this.getRequiredTranslatorBlockAtSocket(0);
		String vpin = translatorBlock.toCode();
		
		if (!(translatorBlock instanceof VirtualPin)) {
			throw new BlockException(translatorBlock.getBlockId(), uiMessageBundle.getString("ardublock.error_msg.VPIN_slot"));
		}

		
		translatorBlock = this.getRequiredTranslatorBlockAtSocket(1);
		String vname = translatorBlock.toCode();
		
		String ret = "Blynk.virtualWrite(" + vpin + ", " + vname + ");\n";
		return  ret;		
	}

}



