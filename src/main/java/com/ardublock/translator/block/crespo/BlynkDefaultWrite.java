
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

public class BlynkDefaultWrite extends TranslatorBlock
{
	private static ResourceBundle uiMessageBundle = ResourceBundle.getBundle("com/ardublock/block/ardublock");
	
	public BlynkDefaultWrite(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
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
		
		String code ="BLYNK_WRITE(" + vpin + ")\n" + 
				    "{\n"; 

		System.out.println(translatorBlock.getClass().getName());
		
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
		// 			  param[0].asLong();
		code +=	"  // The param can contain multiple values, in such case:\n" + 
				"  //int x = param[0].asInt();\n" + 
				"  //int y = param[1].asInt();\n" +
				"\n\n"; 
		
		/*
		BLYNK_WRITE_DEFAULT()
		{
		  int pin = request.pin;      // Which exactly pin is handled?
		  int value = param.asInt();  // Use param as usual.
		}
		*/
		
		TranslatorBlock translatorBlock2 = this.getRequiredTranslatorBlockAtSocket(2);
		while (translatorBlock2 != null)
		{
			code += translatorBlock2.toCode();
			translatorBlock2 = translatorBlock2.nextTranslatorBlock();
		}
		code += "}";

		translator.addDefinitionCommand(code);		
		
		return "Blynk.run();\n";
	}

}



