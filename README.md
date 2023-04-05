# MemoryManagementSystem

This project simulates how memory is managed by the operating system. 

The project has two parts: Manual memory management, and Auto memory management.


## Manual memory management

This part represents a contagious memory that stores processes and deal with it efficiently based on user selection.


_The user can enter a command from the following_:
1- 	RQ command: for adding a new process in the memory with a specified size and algorithm such as F for First Fit. 
2-	RL command: for deleting a process from the memory, but if there is a free space near to the current process the system will compact unused holes into one block automatically.
3-	C command: for compacting any free holes that are not near to each other into one single hole.
4-	STAT command: for showing the state of the memory. 
5-	X command: for exiting the program


The user can also choose a specific algorithm to fill the memory, _the algorithms supported are_:
* First Fit
* Best Fit
* Worst Fit


## Auto memory management

This part translates a logical address to its physical address by using the [Page Table](https://github.com/ReemmaMalki/MemoryManagementSystem/blob/master/correct.txt) , storing a value from the 

correct file into its physical address by taking the logical address from the [Address File](https://github.com/ReemmaMalki/MemoryManagementSystem/blob/master/addresses.txt) .

The system will Store all the signed bytes value to its corresponding physical address by the given logical address
and Retrieve 5 random physical address to check if it's stored in the right corresponds logical address 
(it will repeat the test 5 times for different logical address), 
and Ensure that our program will deal with the partially loaded process by counting the page faults. Finally, the system will
deal with the page replacement and its use when the physical memory is full, and we need to add new page so the system will
automatically choose a page from the page table to do this replacement.
_Replacement is done by some algorithms; we choose FIFO replacement algorithm._
