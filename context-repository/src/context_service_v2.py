#!flask/bin/python

"""RESTful server implemented using the Flask-RESTful extension."""

from flask import Flask, jsonify, abort, request, make_response, url_for
from flask.views import MethodView
from flask.ext.restful import Api, Resource, reqparse, fields, marshal
#from flask.ext.httpauth import HTTPBasicAuth
import json
from dal_sqlite import SQLiteDAL

HTTP_ROUTE_PREFIX = '/context/repository/api/v1.0/'

app = Flask(__name__, static_url_path = "")
api = Api(app)
dal = SQLiteDAL()

#auth = HTTPBasicAuth()
 
#@auth.get_password
# def get_password(username):
#     if username == 'miguel':
#         return 'python'
#     return None
 
# @auth.error_handler
# def unauthorized():
#     return make_response(jsonify( { 'message': 'Unauthorized access' } ), 403)
#     # return 403 instead of 401 to prevent browsers from displaying the default auth dialog

service_fields = {
    'name': fields.String,
    'url': fields.String,
    'description': fields.String,    
    'note': fields.String
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
    }
]

class ContextPushesReciever(Resource):
    #decorators = [auth.login_required]
    
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
         dal.insert_context(json.dumps(args))

api.add_resource(ContextPushesReciever, HTTP_ROUTE_PREFIX + 'push-context', endpoint = 'push')
    
if __name__ == '__main__':
    app.run(debug = True)