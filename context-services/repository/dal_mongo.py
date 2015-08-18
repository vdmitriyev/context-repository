import json
import pymongo
import string
import random

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

	def getUserId(self,login):

		cursor = self.profile_col.find({'login':login})
		for item in cursor:
			id = item['id']
			return id
	

	def addPermission(self):
		print 'addPermission'
		self.permission_col.remove()
		self.service_col.remove()
		self.service_col.insert({'id':1,'name':'Facebook','desc':'social Media'})
		self.service_col.insert({'id':2,'name':'Youtube','desc':'Video'})
		self.service_col.insert({'id':3,'name':'Gmail','desc':'Mailing'})		
		self.permission_col.insert({'sid':1,'uid':113,'perm':-1})
		self.permission_col.insert({'sid':2,'uid':113,'perm':0})
		self.permission_col.insert({'sid':3,'uid':113,'perm':-1})
		self.permission_col.insert({'sid':1,'uid':2,'perm':-1})
		self.permission_col.insert({'sid':1,'uid':3,'perm':-1})


	def checkUser(self,login,pw):
		if self.profile_col.find({'login':login,'pass':pw}).count()>0:
			return True
		return None
		



	def select_services(self,uId):
		serviceList = []

		
		 

			
		
		
		 
		permission =self.permission_col.find( {'uid':uId} )
		for  perm  in permission:
			
			a = self.service_col.find_one({'id':perm['sid']})
			cursor = self.service_col.find({'id':perm['sid']})
			for doc in cursor:
				print a
				serviceList.append({'id':doc['id'],'desc':doc['desc'],'name':doc['name'],'perm':perm['perm']})
			

		return serviceList

		

	def CreateNewUser(self):
		size=6
		chars=string.ascii_uppercase + string.digits

		idu=self.profile_col.count()+1
		pw = ''.join(random.choice(chars) for _ in range(size))
		login =''.join(random.choice(chars) for _ in range(size))
		user =  json.dumps({'id':idu,'login':login,'pass':pw})
		self.profile_col.insert(json.loads(user))
		print login
		print idu
		print pw
		return {'id':idu,'login':login,'pass':pw}

		




	def updateService(self,sid,perm,uid):
		print perm
		print uid
		
		self.permission_col.update({'uid':uid,'sid':sid},
			{'perm':perm,'uid':uid,'sid':sid} 
			)
		
		print 'done'
		print "++++++++++++++++++++++++++++++++++++++++++++++"
		for a in self.permission_col.find():
			print a

		print "++++++++++++++++++++++++++++++++++++++++++++++"
	def insert_context(self, context):
		_id = self.context_col.insert(json.loads(context))
		print 'Context document ID {0}'.format(_id)

	def insert_profile(self, profile):
		_id = self.profile_col.insert(json.loads(profile))
		print 'Profile document ID {0}'.format(_id)


if __name__ == '__main__':
    dal = MongoDAL()
    """dal.insert_context('{"test01":"1"}')	"""