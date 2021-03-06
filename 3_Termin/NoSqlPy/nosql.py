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
        print(">> import finished in %5.2f seconds" %(importTime))

    def importMongo(self, filePath,dbName,coll):
        startTime = time.time()
        file = open(filePath)
        db = self.m_client.get_database(dbName)
        db.drop_collection(coll)
        db.create_collection(coll)
        col = db.get_collection(coll)

        ### read file and import ###
        for line in file.readlines():
            m = json.loads(line)
            col.insert_one(m)

        ### stop Timer ###
        importTime = time.time() - startTime
        print(">> import finished in %5.2f seconds" %(importTime))

    ### redis-db search methods ###
    def searchRedisPLZ(self, plz):
        startTime = time.time()
        print(str(self.r_client.get(plz)))
        print(">> find result in %5.3f seconds" %((time.time()-startTime)))

    def searchRedisCity(self, city):
        startTime = time.time()
        listSize = self.r_client.llen(city)
        plzList = []
        for i in range(listSize):
            plzList.append(self.r_client.lindex(city,i))
        print(str(plzList))
        print(">> find result in %5.3f seconds" %((time.time()-startTime)))

    ### mongo-db search methods
    def searchMongoPLZ(self, plz, dbName, coll):
        startTime = time.time()

        search_argument     = json.loads("{\"_id\" : \""+plz+"\"}")
        search_presentation = json.loads("{\"city\" : 1}")
        db = self.m_client.get_database(dbName)
        lists = db.get_collection(coll).find_one(search_argument,search_presentation)
        try:
            print(str(lists['city']))
        except TypeError:
            print("NONE")
        print(">> find result in %5.3f seconds" %((time.time()-startTime)))

    def searchMongoCity(self, city, dbName, coll):
        startTime = time.time()

        search_argument     = json.loads("{\"city\" : \""+city+"\"}")
        search_presentation = json.loads("{\"_id\" : 1}")
        db = self.m_client.get_database(dbName)
        lists =  db.get_collection(coll).find(search_argument,search_presentation)

        for x in lists: print(">> "+str(x['_id']))
        print(">> find result in %5.3f seconds" %((time.time()-startTime)))

    def showMongoDB(self):
        for x  in self.m_client.database_names(): print(">> DB: "+str(x))

    def showMongoCollection(self, db):
        db = self.m_client.get_database(db)
        for x in db.collection_names(): print(">> Collection: "+str(x))

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
        nosql.showMongoDB()
        dbName = _raw_input(">> Database?: ")
        nosql.showMongoCollection(dbName)
        coll   = _raw_input(">> Collection?: ")
        path = _raw_input(">> Filepath?: ")
        nosql.importMongo(path,dbName, coll)
        sys.exit(0)

elif a1 == nosql.s_plz:
    if a2 == nosql.redis:
        plz = _raw_input(">> PLZ ? | escape with \""+nosql.exit+"\" : ")
        while plz != nosql.exit:
            nosql.searchRedisPLZ(plz)
            plz = _raw_input(">> PLZ ? | escape with \""+nosql.exit+"\" : ")
        sys.exit(0)

    elif a2 == nosql.mongo:
        nosql.showMongoDB()
        dbName = _raw_input(">> Database?: ")
        nosql.showMongoCollection(dbName)
        coll   = _raw_input(">> Collection?: ")
        plz = _raw_input(">> PLZ ? | escape with \""+nosql.exit+"\" : ")
        while plz != nosql.exit:
            nosql.searchMongoPLZ(plz,dbName, coll)
            plz = _raw_input(">> PLZ ? | escape with \""+nosql.exit+"\" : ")
        sys.exit(0)


elif a1 == nosql.s_city:
    if a2 == nosql.redis:
        city = _raw_input(">> City ? | escape with \""+nosql.exit+"\" : ")
        while city != nosql.exit:
            nosql.searchRedisCity(city)
            city = _raw_input(">> City ? | escape with \""+nosql.exit+"\" : ")
        sys.exit(0)

    elif a2 == nosql.mongo:
        nosql.showMongoDB()
        dbName = _raw_input(">> Database?: ")
        nosql.showMongoCollection(dbName)
        coll   = _raw_input(">> Collection?: ")
        city = _raw_input(">> City ? | escape with \""+nosql.exit+"\" : ")
        while city != nosql.exit:
            nosql.searchMongoCity(city,dbName,coll)
            city = _raw_input(">> City ? | escape with \""+nosql.exit+"\" : ")
        sys.exit(0)
nosql.showUsage()