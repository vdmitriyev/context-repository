import json
import pymongo
from pymongo import MongoClient

client = MongoClient('localhost', 27017)
DB_NAME = 'context-repository'
CONTEXT_COL_NAME = 'user-context-col'
PROFILE_COL_NAME = 'user-profile-col'
SERVICE_COL_NAME='service_col'
PERMISSION_COL_NAME='permission_col'



class MongoDAL(object):

	
	def __init__(self):
		self.db = client[DB_NAME]
		self.context_col = self.db[CONTEXT_COL_NAME]
		self.service_col = self.db[SERVICE_COL_NAME]
		self.profile_col = self.db[PROFILE_COL_NAME]
		self.permission_col = self.db[PERMISSION_COL_NAME]
	def select_services(self):
		serviceList = []
		print 'db here we are :p'
		
		for service in self.service_col.find():
			print json.dumps({'id':service['id'],'name':service['name'],'desc':service['desc']})
			serviceList.append({'id':service['id'],'name':service['name'],'desc':service['desc']})

			for a in serviceList:
				print a


				
		return serviceList


		






	def insert_context(self, context):
		_id = self.context_col.insert(json.loads(context))
		print 'Context document ID {0}'.format(_id)

	def insert_profile(self, profile):
		_id = self.profile_col.insert(json.loads(profile))
		print 'Profile document ID {0}'.format(_id)


if __name__ == '__main__':
    dal = MongoDAL()
    """dal.insert_context('{"test01":"1"}')	"""