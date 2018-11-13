
package com.ardublock.translator.block.crespo;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class BlynkAppConnected extends TranslatorBlock
{
	public BlynkAppConnected(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
		this.setNeedsInsertLoopCode(true);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		String code ="//App Conectada\n" +
					"BLYNK_APP_CONNECTED()\n" + 
				    "{\n"; 
		
		TranslatorBlock translatorBlock = this.getTranslatorBlockAtSocket(0);
		while (translatorBlock != null)
		{
			code += translatorBlock.toCode();
			translatorBlock = translatorBlock.nextTranslatorBlock();
		}
		code += "}\n";

		
		code +="//App Desconectada\n" +
				"BLYNK_APP_DISCONNECTED()\n" + 
			    "{\n"; 
	
		translatorBlock = this.getTranslatorBlockAtSocket(1);
		while (translatorBlock != null)
		{
			code += translatorBlock.toCode();
			translatorBlock = translatorBlock.nextTranslatorBlock();
		}
		code += "}";
		
		translator.addDefinitionCommand(code);		
		
		if (BlynkStatic.insertado) {
			return "";
		}
		BlynkStatic.insertado = true;
		return "Blynk.run();\n";
	}
}



