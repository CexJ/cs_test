# cs_test
## Data Engineering Test Auto-complete  
You are given a list of keywords (on the next page). Write a program that will offer up to 4 suggested keywords from the list, starting with the letters typed, not case sensitive. Similar to Google Autocomplete, except that results must be in alphabetical order. You will find examples at the end of this document.  

### Requirements  
● You can use any programming language of your choice, preferably Scala or Java  
● Do not make a web interface for input and output, a simple method taking a string and returning a list of strings is fine    
● Do not use databases and complex libraries, write the completion logic yourself  

### Deliverable  
● Sources of your program, commented when necessary  
● Other artifacts and files required to run the program (like the list of keywords, if your
program loads it)  

### Additional Questions  
● What would you change if the list of keywords was much larger (several millions) ?  

I coded the solution I am presenting keeping in mind simplicity and maintainability.   
The keywords sequence is sorted and stocked in a Vector (to optimize random access). In order to autocomplete a word, the algorithm searches for the index of the smallest word bigger than the prefix. From there the algorithm picks the next four words and checks if they start with the prefix.  
The main idea is to sort out the words just once at the beginning and then use a binary search at each autocompletion. 
This solution I am proposing has the advantage to be easy to read and understood.  

This solution works fine for a small set of keywords, but it won't scale for a very big set of keywords. It is fair to think of a scenario where the set of keywords is too big to be loaded in the memory of a single computer or even stored in a single file.  
In this kind of scenario a distributed approach is needed. A simplistic way to distribute the set of keywords is using the first letters of each different word.  
We can construct recursive tree-like structure where the root is the empty character "" and which has one leaf for each letter. Each possible word is represented as a single node of this tree.  
We can use this structure to distribute our set of keywords by regrouping it by the first letter, then the second letter and so on until it reaches a deeper level in the tree such as the resulting sets are small enough.
The autocomplete algorithm first searches for the correct node. Then it delegates the task to this node.

This algorithm is based on the assumption that we can distribute the set of keywords as we wish. It should not be the case.  
In this scenario we would have to use parallel programming techniques to ask to all nodes to compute their four autocompletions, to collect them and to select the correct four. 

● What would you change if the requirements were to match any portion of the keywords (for example, given the string “pro”, the program could suggest the keyword “reprobe”) ?  

In this case for each word we can generate the list of its suffix and regroup them in tuples such as each suffix points to the first four autocompletion words containing it ((by alphabetical order). We will search the autocompletion of a prefix in the union of the sets pointed by suffix starting with the prefix.  
For example if we take the words "bac", "bbc", "bcc", "bdc", "bec", "cad", "cbd" we will get the following tuple: 
("bac", ["bac"])  
("bbc", ["bbc"])  
("bcc", ["bcc"])  
("bdc", ["bdc"])  
("bec", ["bec"])  
("cad", ["cad"])  
("cbd", ["cbd"])  
("ac", ["bac"])  
("bc", ["bbc"])  
("dc", ["bdc"])  
("ec", ["bec"])  
("ad", ["cad"])  
("bd", ["cbd"])  
("c", ["bac", "bbc", "bcc", "bdc"])  
If we autocomplete "b" we will first recollect 
("bac", ["bac"])  
("bbc", ["bbc"])  
("bcc", ["bcc"])  
("bdc", ["bdc"])  
("bec", ["bec"])  
("bc", ["bbc"])  
("bd", ["cbd"])  
from which we will extract the set ["bac", "bbc", "bcc", "bdc"], if we autocomplete "c" we will recollect
("c", ["bac", "bbc", "bcc", "bdc"])  
from which we will extract the set ["bac", "bbc", "bcc", "bdc"].

This algorithm could be used in a distributed scenario, where as before we divided the set in smaller sets. In this case we will use the suffix as key.  
On the other hand this algorithm introduces some repetitions, in the order of the average length of our keywords, mitigated by taking only the first four words in each tuple.  
Depending on the kind of set of keywords, this could be a reasonable price to pay or not.

As before if it is not possible to choose the partition of the set, we can use a parallel programming technique. We send to all nodes the task of finding the first four words containing the prefix, we collect them and we extract the first four in alphabetical order.

### List of keywords  
project runway  
pinterest  
river  
kayak  
progenex  
progeria  
pg&e  
project free tv  
bank  
proactive  
progesterone  
press democrat  
priceline  
pandora  
reprobe  
paypal  

#### Examples  
##### Autocomplete(“p”)  
pandora  
paypal  
pg&e  
pinterest  

##### Autocomplete(“pr”)  
press democrat  
priceline  
proactive  
progenex  

##### Autocomplete(“pro”)  
proactive  
progenex  
progeria  
progesterone  

##### Autocomplete(“prog”)  
progenex  
progeria  
progesterone  
