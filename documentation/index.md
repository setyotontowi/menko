
# Index
```
1. Features
2. Main File Structure
3. Explanation
```

## Features
This app has 3 main features.
1. In Days Task
   Basically, here where you can add, modify, delete task for particular day. it has range from yesterday until 5 days after.
2. History
   Here, you can see all the task that you've been created. including filtering and summaries
3. Label
   You can add label into your task. In this page, all label you've been created are collected here. You can also filter your task by clicking one of your labels.

## Main File Structure
```
├── SplashActivity
├── MainActivity
│	├── MainFragment
│		└── DayFragment
│	├── HistoryFragment
│	└── LabelFragment
└── Fragment Activity
	└── History Fragment	  
```