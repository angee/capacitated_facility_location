# Capacitated facility location

Code for solving the single-source capacitated facility location problem (SCFLP). 
Uses free open-source MIP solver [CBC](https://projects.coin-or.org/Cbc) through the 
Java interface made available through [or-tools](https://developers.google.com/optimization/).


### Problem summary

Given a set of facilities F and a set of clients S, where the facilities are able to supply the
clients with a commodity for which they have a demand. The problem is to decide which facilities 
to build/open to meet the clients' demand, while not exceeding the facility's capacity,
minimizing the overall costs. 

The overall costs  are composed of two cost sources: (1) the cost of opening the respective 
facilities, and (2) the costs that arise when the respective facilities supply a respective 
client with the commodity (e.g. production and/or transportation costs).

### Building the code

You will need [Java 8](https://www.java.com/en/download/) installed, as well as 
[maven](https://maven.apache.org/). 
Run in your terminal in the main directory of this repository:

    mvn -U clean compile assembly:single
    
This will download all necessary maven libraries (or-tools, etc) and create an executable jar 
file in the (also created directory) `target/`. 


### Running the code

After building the jar file, you can run the code by executing the jar file with the command

    java -jar target/<jarfile-name>.jar 
    
which will solve the Beasley benchmark `cap62`. You can also specify a Beasley benchmark file 
as input, for instance with the command

    java -jar target/<jarfile-name>.jar  src/main/resources/beasley/cap61
    
which will solve the Beasley benchmark `cap61` (see folder 
[src/main/resources/beasley/](src/main/resources/beasley/) for some more Beasley benchmarks). 
You can download all Beasley benchmarks 
[here](http://www.di.unipi.it/optimize/Data/mexch/BeasleyData.zip).