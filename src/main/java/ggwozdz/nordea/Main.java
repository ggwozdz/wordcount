package ggwozdz.nordea;

import ggwozdz.nordea.texttransform.TextTranformModule;
import ggwozdz.nordea.texttransform.TextTransformer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
	private static final Options OPTIONS = setupOptions();
	private static final String HELP_OPT = "help";
	private static final String OUTPUT_FORMAT_OPT = "outputFormat";
	private static final String INPUT_FILE_OPT = "inputFile";
	
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		LOG.info("Application has started");
		
		try {
			CommandLine cmd = new BasicParser().parse(OPTIONS, args);
			if(cmd.hasOption(HELP_OPT)){
				printHelp();
			}else{
				InputStream inputData     = getInputData(cmd);
				OutputFormat outputFormat = getOutputFormat(cmd);
				
				initAndRun(inputData, outputFormat);				
			}				
		} catch (ParseException e) {
			LOG.error("Cannot understand command line args! Reason: {}", e.getMessage());
		} catch (IllegalArgumentException e){
			LOG.error("Invalid input {}", e.getMessage());
		} catch (IOException e) {
			LOG.error("Cannot read input data {}", e.getMessage());
		}
	}
	
	public static void initAndRun(InputStream inputData, OutputFormat outputFormat) throws IOException{
		LOG.info("Bootstraping the application");
		Injector injector = Guice.createInjector(new TextTranformModule());
		TextTransformer textTransformer = injector.getInstance(TextTransformer.class);
		
		LOG.info("Reading data from standard input...");
		textTransformer.processText(inputData, System.out, outputFormat);
	}

	@SuppressWarnings("static-access")
	private static Options setupOptions(){		
		return new Options()
			.addOption(OptionBuilder.withDescription("prints this help message")
				.create(HELP_OPT))			
			.addOption(OptionBuilder.withArgName("file format")						
				.hasArg()
				.withDescription("CSV or XML (defaults to CSV)")
				.create(OUTPUT_FORMAT_OPT))
			.addOption(OptionBuilder.withArgName("/path/to/input")						
				.hasArg()
				.withDescription("a path to input file - if ommited program will read from standard input")
				.create(INPUT_FILE_OPT));
	}
	
	private static void printHelp(){		
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar {jar-name}.jar", OPTIONS, true);		
	}
	
	private static OutputFormat getOutputFormat(CommandLine cmd) {
		String rawOutputFormat    = cmd.getOptionValue(OUTPUT_FORMAT_OPT, OutputFormat.CSV.toString());
		OutputFormat outputFormat = OutputFormat.valueOf(rawOutputFormat.toUpperCase());
		
		LOG.info("Output will be formated as {}", outputFormat);
		
		return outputFormat;
	}
	
	private static InputStream getInputData(CommandLine cmd) throws FileNotFoundException {
		if(cmd.hasOption(INPUT_FILE_OPT)){
			LOG.info("Reading data from {}", cmd.getOptionValue(INPUT_FILE_OPT));
			return new FileInputStream(cmd.getOptionValue(INPUT_FILE_OPT));
		}else{
			LOG.info("Reading data from standard input");
			return System.in;
		}
	}
}
