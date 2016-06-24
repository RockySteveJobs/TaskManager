###How to use?

####1、first step:

init the library in your application or activity

ExcuteTaskManager.getInstance().init();

####2、second step:

copy the file "ExcuteTaskManager" and "ExcuteTask"  to your project 

####3、third step:

write a file extends "ExcuteTask" (eg: JsonExcuteTask)

####4、fourth step:

ExcuteTaskManager.getInstance().newExcuteTask(new JsonExcuteTask());

but if you want callback , please call the method getData()

ExcuteTaskManager.getInstance().getData(new JsonExcuteTask(),
new ExcuteTaskManager.GetExcuteTaskCallback()
{
	public void onDataLoaded(ExcuteTask task)
	{
		//data has int the task, so you can update your ui;
	}
});
