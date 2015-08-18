#!flask/bin/python

"""RESTful server implemented using the Flask-RESTful extension."""
# -*- coding: utf-8 -*-
from flask import Flask, jsonify, abort, request, make_response, url_for
from flask.views import MethodView
from flask.ext.restful import Api, Resource, reqparse, fields, marshal_with,marshal
from flask.ext.httpauth import HTTPBasicAuth
from dal_mongo import MongoDAL
import json


HTTP_ROUTE_PREFIX = '/context/repository/api/v1.0/'

app = Flask(__name__, static_url_path = "")
api = Api(app)
dal = MongoDAL()

auth = HTTPBasicAuth()

@auth.verify_password
def verify_pw(username, password):
    return dal.checkUser(username, password)

 
@auth.error_handler
def unauthorized():
    return make_response(jsonify( { 'message': 'Unauthorized access' } ), 403)
    # return 403 instead of 401 to prevent browsers from displaying the default auth dialog

service_fields = {
    'name': fields.String
,    'url': fields.String,
    'description': fields.String,    
    'note': fields.String
}


profile_fields = {
    'id' : fields.Integer,
    'perm' : fields.Integer
}

services = [
    {
        'name': u'ContextPush',
        'url': HTTP_ROUTE_PREFIX + 'push-context',
        'description': u'Pushing user context to the repository',
        'note': u'Use POST method to push context'
    },
    {
        'name': u'ProfileSave',
        'url': HTTP_ROUTE_PREFIX + 'push-profile',
        'description': u'Pushing user profile to the repository',
        'note': u'Use POST method to push updated profile'
    },

    {
        'name': u'ServicesList',
        'url': HTTP_ROUTE_PREFIX + 'get-services',
        'description': u'Displaying the list of the services related to a given user',
        'note': u'Use POST method to display the services list'
    },

    {
        'name': u'ProfileService',
        'url': HTTP_ROUTE_PREFIX + 'get-services',
        'description': u'Displaying the list of the services related to a given user',
        'note': u'Use POST method to display the services list'
    }
]

class ContextPushesReciever(Resource):
    
    
    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('uid', type = str, location = 'json')
        self.reqparse.add_argument('Time', type = str, location = 'json')
        self.reqparse.add_argument('Location', type = str, location = 'json')
        self.reqparse.add_argument('Content', type = str, location = 'json')
        self.reqparse.add_argument('BatteryLife', type = str, location = 'json')
        super(ContextPushesReciever, self).__init__()

    def get(self):
        return { 'task': marshal(services[0], service_fields) }
        
    def post(self):
        args = self.reqparse.parse_args()
        print args
        dal.insert_context(json.dumps(args))
        return { 'result': True }


class ProfileUpdateReciver(Resource):
    
    decorators = [auth.login_required]
    
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('id', type = int)
        self.parser.add_argument('perm',type=int)


        
        
        
        

        super(ProfileUpdateReciver, self).__init__()
    def get(self):
        uid = dal.getUserId(auth.username())
        
        arr = dal.select_services(uid)
        print 'hello user n: , %s!' %uid 
        return jsonify({'services' :arr})

    
    
    @marshal_with(profile_fields)
    def post(self):

        json_data = request.get_json(force=True)
        for req in json_data:
            dal.updateService(req['id'],req['perm'],dal.getUserId(auth.username()))

            print "service id %d" %req['id']
            print "service perm %d" %req['perm']
        print 'c bon'

        print 'new state'

        print dal.select_services(dal.getUserId(auth.username()))
        return jsonify({'Status' :'ok'})
        

class ProfileService(Resource):
    """docstring for ServicesList"""
    def __init__(self):          
        parser = reqparse.RequestParser()


        print 'halo'
        
        super(ProfileService, self).__init__()        

    def get(self):  
        user = []
        user.append(dal.CreateNewUser())

        
        return {'Profile' :user}

class ServicesList(Resource):
    """docstring for ServicesList"""

    def __init__(self):
        
        parser = reqparse.RequestParser()
        parser.ad


        super(ServicesList, self).__init__()
        
api.add_resource(ProfileService, HTTP_ROUTE_PREFIX + 'edit-profile', endpoint = 'Edit')        
api.add_resource(ContextPushesReciever, HTTP_ROUTE_PREFIX + 'push-context', endpoint = 'push')
api.add_resource(ProfileUpdateReciver, HTTP_ROUTE_PREFIX + 'profile-services', endpoint = 'update')


"""  main  """
if __name__ == '__main__':
    app.run(host= '0.0.0.0',debug = True)

