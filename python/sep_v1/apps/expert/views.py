from django.http import HttpResponse, HttpResponseRedirect
from django.shortcuts import render_to_response
from google.appengine.api import users

import models
import bforms

def respond(request, template, payload):
    user = users.get_current_user()
    if user:
        url = users.create_logout_url(request.path)
        url_linktext = 'Logout'
        payload['user_name'] = user.nickname()
    else:
        url = users.create_login_url(request.path)
        url_linktext = 'Login'
        payload['user_name'] = 'Hi, welcome! Please'
    payload['url'] = url
    payload['url_linktext'] = url_linktext
    payload['recents'] = models.Expert.all().order('-name').fetch(5) 
    return render_to_response(template, payload)

def index(request):
    experts = models.Expert.all().order('-name').fetch(20)
    payload = dict(experts = experts)
    return respond(request, 'index.html', payload)

def create(request):
    if request.method == 'GET':
        expertform = bforms.ExpertForm()
        
    if request.method == 'POST':
        expertform = bforms.ExpertForm(request.POST)
        if expertform.is_valid():
            expert = expertform.save()
            return HttpResponseRedirect(expert.get_absolute_url())
    payload = dict(expertform=expertform)
    return respond(request, 'create.html', payload)

def search(request):
    if request.method == 'GET':
        searchform = bforms.SearchForm()
        
    if request.method == 'POST':
        searchform = bforms.SearchForm(request.POST)
        if searchform.is_valid():
            searchcondition = searchform.save()
            knowledge_area = searchcondition.knowledge_area
            experts = models.Expert.gql("WHERE sap_experience = %:1%",  knowledge_area)
            payload = dict(experts=experts)
            return respond(request, 'search_result.html', payload)
    payload = dict(searchform=searchform)
    return respond(request, 'search.html', payload)

def show(request, expert_key):
    expert = models.Expert.get(expert_key)
    payload = dict(expert=expert)
    return respond(request, 'show.html', payload)

def edit(request, expert_key):
    expert = models.Expert.get(expert_key)
    if request.method == 'GET':
        expertform = bforms.ExpertForm(instance=expert)        
        
    if request.method == 'POST':
        expertform = bforms.ExpertForm(request.POST, instance=expert)
        if expertform.is_valid():
            expertform.save()
            return HttpResponseRedirect(expert.get_absolute_url())
    payload = dict(expertform=expertform)
    return respond(request, 'edit.html', payload)