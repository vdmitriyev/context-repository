import json
import sqlite3


DB_NAME = 'sqlite-context-repository.db'

class SQLiteDAL(object):

	def __init__(self, drop_table=False):
		self.intialize_db(drop_table)

	def intialize_db(self, drop_table):

		connection = sqlite3.connect(DB_NAME)		
		cursor = connection.cursor()

		if drop_table:
			cursor.execute("drop table if exists user_context")

		cursor.execute("select name from sqlite_master WHERE type='table' and name='user_context'")
		rows = cursor.fetchall()
		if not rows:				
			cursor.execute("create table user_context (ID INTEGER PRIMARY KEY AUTOINCREMENT, UserID Int, Time Text, Location Text, Content Text, BatteryLife int)")
		
		cursor.close()
		connection.close()



	def insert_context(self, context):

		json_parsed = json.loads(context)
		user_id = json_parsed.get('uid', 'Unknown')
		ctx_time = json_parsed.get('Time', 'NotSet')
		ctx_location = json_parsed.get('Location', 'NotSet')
		ctx_content = json_parsed.get('Content', 'NotSet')
		ctx_battery_life = json_parsed.get('BatteryLife', 'NotSet')

		connection = sqlite3.connect(DB_NAME)		
		cursor = connection.cursor()

		cursor.execute("INSERT INTO user_context (UserID, Time, Location, Content, BatteryLife) VALUES (?,?,?,?,?)", 
					  (user_id, ctx_time, ctx_location, ctx_content, ctx_battery_life))

		cursor.close()
		connection.commit()
		connection.close()
		

	def print_user_context(self, limit = 10):
		
		connection = sqlite3.connect(DB_NAME)		
		cursor = connection.cursor()
		rows = cursor.execute("SELECT * FROM user_context LIMIT" + str(limit))
		for row in rows:
			print row
		cursor.close()
		connection.close()

	# def insert_profile(self, profile):
	# 	_id = self.profile_col.insert(json.loads(profile))
	# 	print 'Profile document ID {0}'.format(_id)


if __name__ == '__main__':
    dal = SQLiteDAL(drop_table=False)
    dal.insert_context('{  "uid" : 2, "Time": "18:01", "Location": "37 23.516 -122 02.625", "Content": "Skype", "BatteryLife": "90" }')
    dal.print_user_context()