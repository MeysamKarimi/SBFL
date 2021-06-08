# Spectrum-Based Fault Localization (SBFL) Exam Score Calculator

This project is part of SBFL project. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

This program was developed by *Java 11*

## Running the tests

For running the program, ensure two types of input files exist in the ```mutants``` folder:
1. *transitions.txt*: in this file, ```ORIGINAL TRANSITIONS``` and ```MUTANT TRANSITIONS``` are defined. For instance, three transitions exist in a FSM in the following example, where  ```t2```,  ```t3``` and  ```t1``` are mutated respectively in different mutations:
    ```
    ORIGINAL_TRANSITIONS:
    t1,Empty->Normal{[self.capacity>1]put()}
    t2,Normal->Normal{[(self.elements->size<(self.capacity-1))]put(p:Element)}
    t3,Normal->Full{[((self.capacity>1)and(self.elements->size=(self.capacity-1)))]put(p:Element)}    
    ***
    MUTANT_TRANSITIONS:
    outputMutant01:
    t2,Normal->Normal{[(self.elements->size<=(self.capacity-1))]put(p:Element)}    
    outputMutant02:
    t3,Normal->Full{[((self.capacity>1)and(self.elements->size=(self.capacity-1)))]get(p:Element)}
    outputMutant03:
    t1,Empty->Normal{[self.capacity=1]put()}    
    ```
    
    * **Please note that** the input file must be defined in the above format. 
     1. After the keyword ```ORIGINAL_TRANSITIONS:``` in first line, the transitions, each in a line without any space character, where the name and definition of the transition are separated by  ```,``` are defined.
     2. ```***``` sign comes immediately after original transitions.
     3.  Keyword ```MUTANT_TRANSITIONS:``` comes to first to define the mutants. Mutant transitions for each mutation file are defined, each in a line without any space character, where the name and definition of the mutant are separated by ```:```.
     
2. *outputMutant* The name of each file should be the same as it is defined in the  ```MUTANT_TRANSITIONS``` section in  *transitions.txt* file, respectfully.

### Output

The generated result will be saved in ```result.xls``` file in the root path of the project. The file contains ```N + 1``` excel sheets, where ```N``` indicates the number of ```mutants``` and one is for ```Exam Score```.

Each ```mutant``` sheet contains *Error Vector* and *Exam Score* summary for each mutant and the ```Exam Score``` sheet contains summary of all mutants' exam scores.

