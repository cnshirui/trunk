from django.http import HttpResponse, HttpResponseRedirect
from pollango.poll import models
import bforms
from django.shortcuts import render_to_response

def render(template, payload):
    payload['recents'] = models.Poll.all().order('-created_on').fetch(5)
    return render_to_response(template, payload)

def index(request):
    polls = models.Poll.all().order('-created_on').fetch(20)
    payload = dict(polls = polls)
    return render('index.html', payload)

def create(request):
    if request.method == 'GET':
        pollform = bforms.PollForm()
        choiceforms = []
        for i in range(4):
            choiceforms.append(bforms.ChoiceForm(prefix = 'f%s'%i))
    if request.method == 'POST':
        pollform = bforms.PollForm(request.POST)
        choiceform = bforms.ChoiceForm()
        if pollform.is_valid():
            poll = pollform.save()
            choiceforms = []
            for i in range(4):
                choiceforms.append(bforms.ChoiceForm(poll=poll, prefix = 'f%s'%i, data=request.POST))
            for form in choiceforms:
                if form.is_valid():
                    form.save()
            return HttpResponseRedirect(poll.get_absolute_url())
    payload = dict(pollform=pollform, choiceforms=choiceforms)
    return render('create.html', payload)

def poll_detail(request, poll_key):
    poll = models.Poll.get(poll_key)
    choices = models.Choice.all().filter('poll = ', poll)
    if request.method == 'POST':
        choice_key = request.POST['value']
        choice = models.Choice.get(choice_key)
        choice.votes += 1
        choice.put()
        return HttpResponseRedirect('./results/')
    payload = dict(poll = poll, choices = choices)
    return render('poll_details.html', payload)

def poll_results(request, poll_key):
    poll = models.Poll.get(poll_key)
    choices = models.Choice.all().filter('poll = ', poll)
    payload = dict(poll = poll, choices = choices)
    return render('poll_results.html', payload)
