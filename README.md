# wordcount

##compiling
You need maven 3 installed locally. Then run

    mvn clean package
    
to package the application. Once finished, complete and runable application will be available at

    target/nordea-interview-task-1.0.0-full.jar

##running
First option is to ask application to display help message with

    java -jar target/nordea-interview-task-1.0.0-full.jar -help

It will display a help message about usage and parameters:

    usage: java -jar {jar-name}.jar [-help] [-inputFile </path/to/input>]
           [-outputFormat <file format>]
     -help                         prints this help message
     -inputFile </path/to/input>   a path to input file - if ommited program
                               will read from standard input
     -outputFormat <file format>   CSV or XML (defaults to CSV)`
     
Example for ruuning the application

    java -jar target/nordea-interview-task-1.0.0-full.jar < /home/grzegorz/test-data/medium.txt 
    
Note: application will output CSV by default. I order to have it output XML you need to provide 
the output format parameter like this:

    java -jar target/nordea-interview-task-1.0.0-full.jar -outputFormat XML < /home/grzegorz/test-data/medium.txt 

##example output
###XML
Input:

    Ala ma kota. Foo bar baz.    
    
Output:

    <?xml version="1.0" ?>
    <text>
        <sentence>
            <word>Ala</word>
            <word>kota</word>
            <word>ma</word>
        </sentence>
        <sentence>
            <word>bar</word>
            <word>baz</word>
            <word>Foo</word>
        </sentence>
    </text

###CSV
Input:

    Ala ma kota. Foo bar baz xyz.    
    
Output:

    , Word 1, Word 2, Word 3, Word 4
    Sentence 1, Ala,kota,ma
    Sentence 2, bar,baz,Foo,xyz

 
