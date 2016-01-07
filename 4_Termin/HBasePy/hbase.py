import json,sys,happybase,time
from getpass import _raw_input


class nosql:

    h_client = happybase.Conection("localhost")

    help = "-h"
    imp  = "-i"
    hbase  = "hbase"

    s_city = "-c"
    s_plz  = "-p"

    exit   = "exit"

    usage = "nosql [OPTION] [DB]\n"\
            "   "+help+"  : show usage\n"\
            "   "+imp+"   : import from File\n" \
            "    [DB]     : "+hbase+"\n"\
            "   " + s_city + "   : search interactive for city\n"\
            "   " + s_plz + "   : search interactive for plz\n"\
            "   " + exit +"   : escape interactive search\n"

    def importHbase(self, table, filePath):
        # TODO
        startTime = time.time()
        file = open(filePath)

        ### read file and import ####
        for line in file.readlines():
            m = json.loads(line)

            # key = m['_id']
            # val = m['city']

            # self.r_client.set(key,val)
            # self.r_client.rpush(val,key)

        ### stop Timer ###
        importTime = time.time() - startTime
        print(">> import finished in %5.3f seconds" %(importTime))

    ### hbase-db search methods ###
    def searchHBasePLZ(self, table, plz):
        # TODO
        startTime = time.time()
        print(str(self.r_client.get(plz)))
        print(">> find result in %5.3f seconds" %((time.time()-startTime)))

    def searchHBaseCity(self, table ,city):
        # TODO
        startTime = time.time()
        listSize = self.r_client.llen(city)
        plzList = []
        for i in range(listSize):
            plzList.append(self.r_client.lindex(city,i))
        print(str(plzList))
        print(">> find result in %5.3f seconds" %((time.time()-startTime)))

    def showHBaseTables(self):
        # TODO
        for x  in self.m_client.database_names(): print(">> DB: "+str(x))


    def showUsage(self):
        print(self.usage)
        sys.exit(1)


#### MAIN ####
nosql = nosql()
args = sys.argv

# wrong number of arguments
if len(args) != 3:
    nosql.showUsage()

a1 = args[1]
a2 = args[2]

if a1 == nosql.help: nosql.showUsage()
elif a1 == nosql.imp:
    if a2 == nosql.hbase:
        # show tables
        nosql.showHBaseTables()
        table = _raw_input(">> Table?: ")
        # get file
        path = _raw_input(">> Filepath?: ")
        nosql.importHbase(table,path)
        sys.exit(0)
    else: nosql.showUsage()

elif a1 == nosql.s_plz:
    if a2 == nosql.hbase:
        # show tables
        nosql.showHBaseTables()
        table = _raw_input(">> Table?: ")
        # start interactive search
        plz   = _raw_input(">> PLZ ? | escape with \""+nosql.exit+"\" : ")
        while plz != nosql.exit:
            nosql.searchHBasePLZ(table,plz)
            plz = _raw_input(">> PLZ ? | escape with \""+nosql.exit+"\" : ")
        sys.exit(0)

    else: nosql.showUsage()

elif a1 == nosql.s_city:
    if a2 == nosql.hbase:
        # show tables
        nosql.showHBaseTables()
        table = _raw_input(">> Table?: ")
        # start interactive search
        city = _raw_input(">> City ? | escape with \""+nosql.exit+"\" : ")
        while city != nosql.exit:
            nosql.searchHBaseCity(table,city)
            city = _raw_input(">> City ? | escape with \""+nosql.exit+"\" : ")
        sys.exit(0)

    else: nosql.showUsage()
nosql.showUsage()