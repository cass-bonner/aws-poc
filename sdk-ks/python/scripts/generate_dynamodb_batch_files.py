#!/usr/bin/python
import fileinput
import re
#import os.stat
# -*- coding: utf-8 -*-

######################################################
# Just a hack/sample to # generate batch dynammo files  
######################################################

table_name="Aminalz"
input_file='data.json'
header = "{ \"" +table_name + "\": [\n"
footer="]}"
modvalue=25
num_lines = sum(1 for line in open(input_file))

#print num_lines 

f=open(input_file,'r')
i=0
rangeindex=modvalue-1 #index0

contents = f.readlines();

while i<=num_lines:
  generated_file="data/input_file" + `i` + ".json"
  new_file=open(generated_file,'w')
  #print "generated_file: " + `generated_file`
  #print "content: " + `contents`
  lines = contents[slice(i,rangeindex)]
  #print "lines: " + `lines`
  lines = "".join(lines)
  rlines = re.sub(",$","",lines)
  new_file.write(header)
  new_file.write(rlines);
  new_file.write(footer)
   
  if (i%modvalue==0):
    new_file.close();
    i=i+modvalue
    rangeindex=rangeindex+modvalue
    #print "rangindex: " + `rangeindex`
    #print "i: " + `i`

f.close()
