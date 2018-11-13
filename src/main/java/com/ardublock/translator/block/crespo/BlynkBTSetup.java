
package com.ardublock.translator.block.crespo;

import java.util.ResourceBundle;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.variable_String;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class BlynkBTSetup extends TranslatorBlock
{
	private static ResourceBundle uiMessageBundle = ResourceBundle.getBundle("com/ardublock/block/ardublock");
	
	public BlynkBTSetup(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{

		TranslatorBlock serialBlock = this.getRequiredTranslatorBlockAtSocket(0);
		TranslatorBlock translatorBlock = this.getTranslatorBlockAtSocket(2);
		
		if (translatorBlock instanceof com.ardublock.translator.block.TrueBlock) {
			translator.addDefinitionBefore("#define BLYNK_PRINT Serial");
			//translator.addDefinitionBefore("#define BLYNK_DEBUG");
			translator.addSetupCommand("Serial.begin(9600);");			
		}
		
		translator.addDefinitionCommand("//Download link for Blynk library https://github.com/blynkkk/blynk-library/releases/latest\n");
		//translator.addDefinitionCommand("#define SERIAL_BAUD 9600");
		
		if (serialBlock instanceof com.ardublock.translator.block.crespo.SoftwareSerial) {
			translator.addHeaderFile("SoftwareSerial.h");
			translator.addDefinitionCommand("SoftwareSerial SerialBLE(" + serialBlock.toCode() + "); // RX, TX");
		} else if (serialBlock instanceof com.ardublock.translator.block.crespo.SerialPort) {
			if (serialBlock.toCode().equals("Serial0"))
				translator.addDefinitionCommand("#define SerialBLE Serial");
			else
				translator.addDefinitionCommand("#define SerialBLE Serial1");
		} else {
			throw new BlockException(serialBlock.getBlockId(), uiMessageBundle.getString("bg.crespo.error_msg.serial_needed"));
		}
		
		translatorBlock = this.getRequiredTranslatorBlockAtSocket(1);
		String auth = translatorBlock.toCode();		
		if (translatorBlock instanceof variable_String) {
			 auth += ".c_str()";
		}
		
		translator.addHeaderFile("BlynkSimpleSerialBLE.h");	

		translator.registerBodyTranslateFinishCallback(this);
		
		return "SerialBLE.begin(BLYNK_BAUD);\n" +
		   	   "Blynk.begin(SerialBLE, " + auth  + ");\n";
	}
	
	@Override
	public void onTranslateBodyFinished()
	{
		BlynkStatic.insertado = false;
	}
}



