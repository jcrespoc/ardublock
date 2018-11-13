
package com.ardublock.translator.block.crespo;

import java.util.ResourceBundle;

import com.ardublock.core.Context;
import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.variable_String;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class BlynkESP8266Setup extends TranslatorBlock
{
	private static ResourceBundle uiMessageBundle = ResourceBundle.getBundle("com/ardublock/block/ardublock");
	
	public BlynkESP8266Setup(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);		
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{

		TranslatorBlock serialBlock = this.getRequiredTranslatorBlockAtSocket(0);		
		TranslatorBlock translatorBlock = this.getTranslatorBlockAtSocket(4);

		if (translatorBlock instanceof com.ardublock.translator.block.TrueBlock) {
			translator.addDefinitionBefore("#define BLYNK_PRINT Serial");
			//translator.addDefinitionBefore("#define BLYNK_DEBUG");
			translator.addSetupCommand("Serial.begin(9600);");
		}

		translator.addDefinitionCommand("//Download link for Blynk library https://github.com/blynkkk/blynk-library/releases/latest\n");
		//translator.addDefinitionCommand("#define ESP8266_BAUD 115200\n");
		
		if (serialBlock instanceof com.ardublock.translator.block.crespo.SoftwareSerial) {
			serialBlock = this.getRequiredTranslatorBlockAtSocket(0);
			translator.addHeaderFile("SoftwareSerial.h");
			translator.addDefinitionCommand("SoftwareSerial EspSerial(" + serialBlock.toCode() + "); // RX, TX");
		} else if (serialBlock instanceof com.ardublock.translator.block.crespo.SerialPort) {
			if (serialBlock.toCode().equals("Serial0")) {
				System.out.println(serialBlock.toCode());
				translator.addDefinitionCommand("#define EspSerial Serial");
			} else {
				System.out.println(serialBlock.toCode());
				translator.addDefinitionCommand("#define EspSerial Serial1");
			}
		} else {
			throw new BlockException(serialBlock.getBlockId(), uiMessageBundle.getString("bg.crespo.error_msg.serial_needed"));
		}
		
		
		translatorBlock = this.getRequiredTranslatorBlockAtSocket(1);
		String ssid = translatorBlock.toCode();
		if (translatorBlock instanceof variable_String) {
			 ssid += ".c_str()";
		}

		translatorBlock = this.getRequiredTranslatorBlockAtSocket(2);
		String passwd = translatorBlock.toCode();
		if (translatorBlock instanceof variable_String) {
			 passwd += ".c_str()";
		}

		translatorBlock = this.getRequiredTranslatorBlockAtSocket(3);
		String auth = translatorBlock.toCode();		
		if (translatorBlock instanceof variable_String) {
			 auth += ".c_str()";
		}
		
		translator.addHeaderFile("ESP8266_Lib.h");
		translator.addHeaderFile("BlynkSimpleShieldEsp8266.h");		
		
		translator.addDefinitionCommand("ESP8266 wifi(&EspSerial);");

		translator.registerBodyTranslateFinishCallback(this);
		
		return ("EspSerial.begin(BLYNK_BAUD);\n" +
				   "delay(10);\n" +
				   "Blynk.begin(" + auth  + ", wifi, " + ssid +", " + passwd + ");\n");
	}

	@Override
	public void onTranslateBodyFinished()
	{
		BlynkStatic.insertado = false;
	}
}



