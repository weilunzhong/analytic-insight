# Usage example

## Build docker image

* docker build -t "carddeck:\<tag\>" .

## Generate cards

* docker run -v \<host_data_folder\>:/data carddeck:\<tag\> sh run.sh /data/\<interactions.json\> /data/\<users.json\> /data/\<contents.json\> /data/\<cards.json\>

# Why

###### Incremental complexity

* As I see it, this module do not have to be large or complex. Though, there are benefits to having a structure that it is easy to extend in capability with minimal changes in the interfaces facing outwards from the module (interfaces as in expectation by other services what is valid input and output).

* This allows us to ship fast and evaluate our assumptions before adding complexity, without taking too many steps backward when we learn more about the problem we want to solve and new features are needed as we go.

###### Independence between story writers

* This increases the efficiency of parallel development work where it is the most likely to happen; writing stories (cardwriter functions).

###### Independence between story writing and data layers

* For this, interfaces facing outwards need to be unlikely to need external adaption for individual stories, without losing expressive power.

* As development progresses, outward-facing interfaces are however bound to need some change. E.g. due to introduction of new valid Components, that this module and the frontend must agree upon. However, keeping the number and complexity of those agreements to a minimum would yield benefits in faster and less error-prone code development and a more predictable runtime environment.

* If the structural contracts to other modules are kept to a minimum, it becomes more maintainable to validate inputs/outputs and easier to find when and where things go wrong.

###### Minimal coupling to other modules

* Minimal amount of shared structural definitions with other modules.

* Allows for faster, independent and more future-proof development of the modules internal code and the possibility to optimize for the purpose of the module.

###### Story writing safety

* Many stories will be written, by different people. To greatly reduce the risk of output errors, structure of cards should be enforced. The lower code complexity and maintenance needs to perform this enforcement, the better.

* A very robust, low maintenance and developer-friendly way to do this is using a type-safe language. The sole purpose of this module is data transformation of predictably structured input to predictably structured output; exactly what static type-safety is for. Dynamic typing often has benefits in domains where input and/or output structure is unpredictable (e.g. has missing fields, missing values, loose structure). An example of a benefit for dynamic typing is that often less code is needed to manipulate loose structures. For those benefits, safety is traded away, where more validation of assumptions are required, code is less safe to refactor, it’s easier to introduce bugs and developers get less help from language and tools to reason about the code. Since the interfaces of the internal cardwriter functions have the same property as the outward-facing interface (predictable input and output structure), the benefits of static typing for this module are even greater. Furthermore, static typing provides maintenance free compile-time validation of the internal code structure of each cardwriter function.

###### Statelessness

* To reduce risk of errors due to e.g. old or corrupt state, misconfiguration, code complexity or external dependence complexity.

* One input file, one output file.

* In the best scenario this module would only be one “moving part”, one logical function called by an external process; this is possible since the necessary scope of the module is small. This reduces complexity (thus increases maintainability and decreases risk of bugs and incorrect data propagation) by reducing external code dependency, increasing system state predictability, reducing the risk of old state being used and reducing the risk of misconfiguration of the system.

###### Favor third-party code use

* To narrow down the problems we need to focus on, to increase quality and development speed

* Data transformation is a widespread endeavour worked on by many in the software community. Frameworks and programming languages have been developed in large open-source projects for this purpose over many years. Since for this module, the purpose is stateless, safe, independent, heterogenous data transformation, the functional paradigm would be extremely suitable. Furthermore, when computational scalability is needed, due to the stateless and independent nature of the transformation functions (card writers) they are very easy to parallelize both locally and in a distributed manner through already existent, modern and widely used open-source tools. 

![Alt text](/images/illustration.png?raw=true "Optional Title")

## Usage
![Alt text](/images/invoker.png?raw=true "Optional Title")
