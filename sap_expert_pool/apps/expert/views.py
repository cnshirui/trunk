from django.http import HttpResponse, HttpResponseRedirect
import models
import bforms
from django.shortcuts import render_to_response

def render(template, payload):
    payload['recents'] = models.Expert.all().order('-name').fetch(5)
    return render_to_response(template, payload)

def index(request):
    experts = models.Expert.all().order('-name').fetch(20)
    payload = dict(experts = experts)
    return render('index.html', payload)

def create(request):
    if request.method == 'GET':
        expertform = bforms.ExpertForm()
        
    if request.method == 'POST':
        expertform = bforms.ExpertForm(request.POST)
        if expertform.is_valid():
            expert = expertform.save()
            return HttpResponseRedirect(expert.get_absolute_url())
    payload = dict(expertform=expertform)
    return render('create.html', payload)

def search(request):
    if request.method == 'GET':
        searchform = bforms.SearchForm()
        
    if request.method == 'POST':
        searchform = bforms.SearchForm(request.POST)
        if searchform.is_valid():
            searchcondition = searchform.save()
            con1 = searchcondition.con1
            con2 = searchcondition.con2
            #experts = models.Expert.gql("WHERE city = :city1 or city = :city2", city1=con1, city2=con2)
            experts = models.Expert.gql("WHERE city = :1", "*hai") 
            payload = dict(experts=experts)
            return render('search_result.html', payload)
    payload = dict(searchform=searchform)
    return render('search.html', payload)

#def search_result(request):

def expert_detail(request, expert_key):
    expert = models.Expert.get(expert_key)
    if request.method == 'POST':
        return HttpResponseRedirect('./results/')
    payload = dict(expert = expert)
    return render('expert_details.html', payload)