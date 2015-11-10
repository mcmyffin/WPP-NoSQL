import json,sys,collections,redis,time

from getpass import _raw_input
from pymongo import MongoClient

class nosql:

    r_client = redis.StrictRedis(host='localhost', port=6379, db=0)
    m_client = MongoClient(host='localhost',port=27017)
    m_db         = "test"
    m_collection = "plzCity"

    help = "-h"
    imp  = "-i"

    redis = "redis"
    mongo = "mongo"

    s_city = "-c"
    s_plz  = "-p"

    exit   = "exit"

    usage = "nosql [OPTION] [DB]\n"\
            "   "+help+"  : show usage\n"\
            "   "+imp+"   : import from File\n" \
            "    [DB]     : "+redis+","+mongo+"\n"\
            "   " + s_city + "   : search interactive for city\n"\
            "   " + s_plz + "   : search interactive for plz\n"\
            "   " + exit +"   : escape interactive search\n"


    def importRedis(self, filePath):
        startTime = time.time()
        file = open(filePath)

        ### read file and import ####
        for line in file.readlines():
            m = json.loads(line)

            key = m['_id']
            val = m['city']

            self.r_client.set(key,val)
            self.r_client.rpush(val,key)

        ### stop Timer ###
        importTime = time.time() - startTime
        print("import finished in %5.2f seconds" %(importTime))

    def importMongo(self, filePath,dbName):
        startTime = time.time()
        file = open(filePath)
        db = self.m_client.get_database(self.dbName)
        db.drop_collection(self.m_collection)
        db.create_collection(self.m_collection)
        col = db.get_collection(self.m_collection)

        ### read file and import ###
        for line in file.readlines():
            m = json.loads(line)
            col.insert_one(m)

        ### stop Timer ###
        importTime = time.time() - startTime
        print("import finished in %5.2f seconds" %(importTime))

    ### redis-db search methods ###
    def searchRedisPLZ(self, plz):
        return self.r_client.get(plz)

    def searchRedisCity(self, city):
        listSize = self.r_client.llen(city)
        plzList = []
        for i in range(listSize):
            plzList.append(self.r_client.lindex(city,i))
        return plzList

    ### mongo-db search methods
    def searchMongoPLZ(self, plz, dbName):
        search_argument     = json.loads("{\"_id\" : \""+plz+"\"}")
        search_presentation = json.loads("{\"city\" : 1}")
        db = self.m_client.get_database(self.dbName)
        lists = db.get_collection(self.m_collection).find_one(search_argument,search_presentation)
        try:
            return lists['city']
        except TypeError:
            return

    def searchMongoCity(self, city, dbName):
        search_argument     = json.loads("{\"city\" : \""+city+"\"}")
        search_presentation = json.loads("{\"_id\" : 1}")
        db = self.m_client.get_database(self.dbName)
        lists =  db.get_collection(self.m_collection).find(search_argument,search_presentation)

        for x in lists: print(">> "+str(x['_id']))

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
    if a2 == nosql.redis:
        path = _raw_input(">> Filepath?: ")
        nosql.importRedis(path)
        sys.exit(0)
    elif a2 == nosql.mongo:
        dbName = _raw_input(">> DB?: ")
        path = _raw_input(">> Filepath?: ")
        nosql.importMongo(path,dbName)
        sys.exit(0)

elif a1 == nosql.s_plz:
    if a2 == nosql.redis:
        plz = _raw_input(">> PLZ ? | escape with \""+nosql.exit+"\" : ")
        while plz != nosql.exit:
            city = nosql.searchRedisPLZ(plz)
            print(">> "+str(city))
            plz = _raw_input(">> PLZ ? | escape with \""+nosql.exit+"\" : ")
        sys.exit(0)

    elif a2 == nosql.mongo:
        dbName = _raw_input(">> DB?: ")
        plz = _raw_input(">> PLZ ? | escape with \""+nosql.exit+"\" : ")
        while plz != nosql.exit:
            city = nosql.searchMongoPLZ(plz,dbName)
            print(">> "+str(city))
            plz = _raw_input(">> PLZ ? | escape with \""+nosql.exit+"\" : ")
        sys.exit(0)


elif a1 == nosql.s_city:
    if a2 == nosql.redis:
        city = _raw_input(">> City ? | escape with \""+nosql.exit+"\" : ")
        while city != nosql.exit:
            plzList = nosql.searchRedisCity(city)
            print(">> "+str(plzList))
            city = _raw_input(">> City ? | escape with \""+nosql.exit+"\" : ")
        sys.exit(0)

    elif a2 == nosql.mongo:
        dbName = _raw_input(">> DB ?: ")
        city = _raw_input(">> City ? | escape with \""+nosql.exit+"\" : ")
        while city != nosql.exit:
            plzList = nosql.searchMongoCity(city,dbName)
            # print(">> "+str(plzList))
            city = _raw_input(">> City ? | escape with \""+nosql.exit+"\" : ")
        sys.exit(0)
nosql.showUsage()