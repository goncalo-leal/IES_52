
This file has detailed information about the api endpoint's

SensorData controller -> This class has the most points for data performed with stats

@PostMapping("/addSensorData/{pid}") 
  -> Parameters: pid -> interger, which is the  sensor id
  -> RequestBody: SensorData s , needs to receive a field "data", which is a string with the action that was taken 
  -> return:  SensorData  add a new sensorData to the database

@GetMapping("/PeopleInShopping/{pid}")
  -> Parameters: pid -> interger, which is the  shopping id
  -> return : integer , which is the number of people in shopping

@GetMapping("/PeopleInStore/{pid}")
  -> Parameters: pid -> interger, which is the  store id
  -> return : integer , which is the number of people in store

 @GetMapping("/PeopleInPark/{pid}")
  -> Parameters: pid -> interger, which is the park id
  -> return : integer , which is the number of people in park

@GetMapping("/PeopleInShoppingInLastHour/{pid}")
  -> Parameters: pid -> interger, which is the  shopping id
  -> return : integer , which is the number of people in shopping in the last hour
  
@GetMapping("/PeopleInShoppingToday/{pid}")
  -> Parameters: pid -> interger, which is the  shopping id
  -> return : integer , which is the number of people who entered in shopping today
 
@GetMapping("/PeopleInShoppingToday/{pid}/{hours}")
  -> Parameters: pid -> interger, which is the  shopping id; hours ->  interger, which is the  hours we want to see 
  -> return : integer , which is the number of people who entered in shopping in the  hour we want 

@GetMapping("/PeopleInShoppingTodayCompareWithLaskWeek/{pid}")
  -> Parameters: pid -> interger, which is the  shopping id
  -> return : map<string,integer> , "today":  number of people who entered in shopping today ; "laskWeek":number of people who entered in shopping 7 days ago

@GetMapping("/PeopleInShoppingTodayCompareWithLaskWeek/{pid}/{hours}")
  -> Parameters: pid -> interger, which is the  shopping id; hours ->  interger, which is the  hours we want to see 
  -> return  Map<String,Integer>,  "today":  number of people who entered in shopping today within the given hours ; "laskWeek":number of people who entered in shopping 7 days ago within the given hours
 
 //esta deve estar mal vi agora 
@GetMapping("/PeopleInShoppingLast7Days/{pid}")
  -> Parameters: pid -> interger, which is the  shopping id
  -> return : Map<String,integer> , which is the number of people who entered in shopping in the last 7 days, in every single day like {"monday":1000, ...}

//este tambem tem 1 mini erro 
@GetMapping("/PeopleInShoppingWeek/{pid}")
  -> Parameters: pid -> interger, which is the  shopping id
  -> return : integer , which is the number of people who entered in shopping in the last 7 days 
  

Same features  for parks and stores
  
  
  
