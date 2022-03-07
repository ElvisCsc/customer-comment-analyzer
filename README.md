Java multithreaded Application that analyze customer online reviews, will run through all the comments and create a report with totals to be used for business intelligence and marketing. 

metrics that we keep track of:

  - Total number of comments that are shorter than 15 characters
  - Total number of comments that refer to the "Mover" device
  - Total number of comments that refer to the "Shaker" device
  - Total number of comments that refer to the "QUESTIONS" device
  - Total number of comments that refer to the "SPAM" device

The daily comments are stored in text files under the **docs** directory within the project. Each line represents a single comment by a customer. All files in the directory get analyzed and the report gets printed to the console
