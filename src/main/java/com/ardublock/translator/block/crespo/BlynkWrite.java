
package com.ardublock.translator.block.crespo;

import java.util.ResourceBundle;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.VariableFakeBlock;
import com.ardublock.translator.block.VariableNumberBlock;
import com.ardublock.translator.block.VariableNumberUnsignedLongBlock;
import com.ardublock.translator.block.VariableStringBlock;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class BlynkWrite extends TranslatorBlock
{
	private static ResourceBundle uiMessageBundle = ResourceBundle.getBundle("com/ardublock/block/ardublock");
	
	public BlynkWrite(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
		this.setNeedsInsertLoopCode(true);
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
		
		String code ="//Escribe una variable desde la app\n" +
					"BLYNK_WRITE(" + vpin + ")\n" + 
				    "{\n"; 

		
		if (translatorBlock instanceof VariableNumberBlock) {
			code += "  " + vname + " = param.asInt(); // Get value as integer\n";
		} else if (translatorBlock instanceof VariableNumberUnsignedLongBlock) {
			code += "  " + vname + " = param.asLong(); // Get value as integer\n";
		} else if (translatorBlock instanceof VariableFakeBlock) {
			code += vname + "[0] = param[0].asInt();\n" + 
					vname + "[1] = param[1].asInt();\n" +
					vname + "[2] = param[2].asInt();\n";
		} else if (translatorBlock instanceof VariableStringBlock) {
			code += vname + "[0] = param.asStr();\n";
		} else {
			throw new BlockException(translatorBlock.getBlockId(), uiMessageBundle.getString("ardublock.error_msg.variable_needed"));
		}
		
		// String i = param.asStr();
		// double d = param.asDouble();
		// int i =  param.asInt();
		
		TranslatorBlock translatorBlock2 = this.getTranslatorBlockAtSocket(2);
		while (translatorBlock2 != null)
		{
			code += translatorBlock2.toCode();
			translatorBlock2 = translatorBlock2.nextTranslatorBlock();
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



