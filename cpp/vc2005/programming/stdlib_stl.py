#!/usr/bin/python
def point(meta):return meta[0]
def rep(meta):return meta[1]
def prec(meta):return meta[2]

whether_perm=1
one_solution=1
which_point=24

def make_enumerate(array,start,end):
    if(start==end):
        return [(array[start],str(array[start]),'SINGLE')]
    else:
        res=[]
        for i in range(start,end):
            part1_list=make_enumerate(array,start,i)
            part2_list=make_enumerate(array,i+1,end)
            for part1 in part1_list:
                for part2 in part2_list:
                    # try +
                    p=point(part1)+point(part2)
                    nrep=rep(part1)+"+"+rep(part2)
                    nprec="ADD"
                    res.append((p,nrep,nprec))

                    #try -
                    p=point(part1)-point(part2)
                    if(prec(part2)=="ADD" or
                       prec(part2)=="SUB"):
                        nrep=rep(part1)+"-("+rep(part2)+")"
                    else:
                        nrep=rep(part1)+"-"+rep(part2)
                    nprec="SUB"
                    res.append((p,nrep,nprec))

                    #try *
                    p=point(part1)*point(part2)
                    if(prec(part1)=="ADD" or
                       prec(part1)=="SUB"):
                        tmp1="("+rep(part1)+")"
                    else:
                        tmp1=rep(part1)
                    if(prec(part2)=="ADD" or
                       prec(part2)=="SUB"):
                        tmp2="("+rep(part2)+")"
                    else:
                        tmp2=rep(part2)
                    nrep=tmp1+"*"+tmp2
                    nprec="MUL"
                    res.append((p,nrep,nprec))

                    #try /
                    if(point(part2)!=0 and
                       point(part1)%point(part2)==0):
                        p=point(part1)/point(part2)
                        if(prec(part1)=="ADD" or
                           prec(part1)=="SUB"):
                            tmp1="("+rep(part1)+")"
                        else:
                            tmp1=rep(part1)
                        if(prec(part2)=="ADD" or
                           prec(part2)=="SUB"):
                            tmp2="("+rep(part2)+")"
                        else:
                            tmp2=rep(part2)
                        nrep=tmp1+"/"+tmp2
                        nprec="DIV"
                        res.append((p,nrep,nprec))
        return res

#permutate the array,and enumerate array using each permutation:-)
def permutation(array,start,end,it):
    global one_solution
    if(it==end):
        res=make_enumerate(array,start,end)
        return res
    else:
        res=[]
        for i in range(it,end+1):
            tmp=array[it]
            array[it]=array[i]
            array[i]=tmp
            res_tmp=permutation(array,start,end,it+1)
            tmp=array[it]
            array[it]=array[i]
            array[i]=tmp
            if(res_tmp!=[] and one_solution==1):
                return res_tmp
            else:
                res.extend(res_tmp)
        return res

def make_filter(res):
    global which_point
    known_result=[] #eliminate the duplicate:-)
    new_res=[]
    for res_record in res:
        if(point(res_record)==which_point and
           not (rep(res_record) in known_result)):
            new_res.append(res_record)
            known_result.append(rep(res_record))
    return new_res

if __name__=="__main__":
    array=[4,3,2,1,5,7]
    if(whether_perm):
        res=permutation(array,0,len(array)-1,0)
    else:
        res=make_enumerate(array,0,len(array)-1)
    res=make_filter(res)
    print res