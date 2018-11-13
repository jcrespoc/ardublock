package com.ardublock.translator.block.crespo;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;


public class SerialPort extends TranslatorBlock {

	public SerialPort (Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}
	//@Override
			public String toCode() throws SocketNullException, SubroutineNotDeclaredException
			{
				
				String ret = label;
				
				TranslatorBlock translatorBlock = this.getRequiredTranslatorBlockAtSocket(0);
				translator.addDefinitionCommand("#define BLYNK_BAUD " + translatorBlock.toCode() + "\n");
				
				return codePrefix + ret + codeSuffix;
					
			}
	
}
