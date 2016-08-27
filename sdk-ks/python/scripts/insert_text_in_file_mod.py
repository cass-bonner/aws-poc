#!/usr/bin/python
import fileinput
#import os.stat

######################################################
# Just a hack/sample to # insert text at mod location  
######################################################

input_file='testdata.txt'
newtext = "text_to_insert\n"
modvalue=25
num_lines = sum(1 for line in open(input_file))

#print num_lines 

f=open(input_file,'r')
contents=f.readlines()
f.close()

#for line in fileinput.input():
#    pass

i=1
while i<=num_lines:
  if (i%modvalue==0):
    contents.insert(i,newtext); 
    i=i+1
    num_lines = len(contents) 
    print "num_lines: " + `num_lines`
    
  i=i+1;
  print "ii " + `i`; 
f=open(input_file,'w')
contents = "".join(contents)
f.write(contents)



