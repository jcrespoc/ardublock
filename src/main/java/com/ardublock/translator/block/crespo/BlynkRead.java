
package com.ardublock.translator.block.crespo;

import java.util.ResourceBundle;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.VariableNumberBlock;
import com.ardublock.translator.block.VariableNumberUnsignedLongBlock;
import com.ardublock.translator.block.VariableStringBlock;
import com.ardublock.translator.block.VariableVectorBlock;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class BlynkRead extends TranslatorBlock
{
	private static ResourceBundle uiMessageBundle = ResourceBundle.getBundle("com/ardublock/block/ardublock");
	
	public BlynkRead(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
		this.setNeedsInsertLoopCode(true);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{

		TranslatorBlock translatorBlock1 = this.getRequiredTranslatorBlockAtSocket(0);
		String vname = translatorBlock1.toCode();


		TranslatorBlock translatorBlock2 = this.getRequiredTranslatorBlockAtSocket(1);

		if (!(translatorBlock2 instanceof VirtualPin)) {
			throw new BlockException(translatorBlock2.getBlockId(), uiMessageBundle.getString("ardublock.error_msg.VPIN_slot"));
		}
		String vpin = translatorBlock2.toCode();

		
		String code ="//Lee una variable desde la app\n" +
					 "BLYNK_READ(" + vpin + ")\n" + 
				     "{\n"; 

	
		TranslatorBlock translatorBlock3 = this.getTranslatorBlockAtSocket(2);
		while (translatorBlock3 != null)
		{
			code += translatorBlock3.toCode();
			translatorBlock3 = translatorBlock3.nextTranslatorBlock();
		}
		
		if (translatorBlock1 instanceof VariableVectorBlock) {
			code += "  Blynk.virtualWrite(" + vpin + ", " + vname + ");\n";
		} else if (translatorBlock1 instanceof VariableNumberBlock) {
			code += "  Blynk.virtualWrite(" + vpin + ", " + vname + ");\n";
		} else if (translatorBlock1 instanceof VariableNumberUnsignedLongBlock) {
			code += "  Blynk.virtualWrite(" + vpin + ", " + vname + ");\n";
		} else if (translatorBlock1 instanceof VariableStringBlock) {
			code += "  Blynk.virtualWrite(" + vpin + ", " + vname + ");\n";
		} else {
			throw new BlockException(translatorBlock1.getBlockId(), uiMessageBundle.getString("bg.crespo.error_msg.variable_needed"));
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



