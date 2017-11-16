var express = require('express');
var db = require("seraph")({
    server: "http://localhost:7474",
    user: 'neo4j',
    pass: 'test'
});
var bodyParser = require('body-parser');
var numeral = require('numeral');
var app = express();

app.use(bodyParser.json());
app.all('*', function(req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Methods', 'PUT, GET, POST, DELETE, OPTIONS');
    res.header('Access-Control-Allow-Headers', 'X-Requested-With, content-type, Authorization');
    next();
});
app.listen(2000, function() {
    console.log("Serwer uruchomiony na localhost:2000");
});

app.get('/students', function(req, res) {
    var query;
    if (req.query.sort) {
        query = "MATCH (u:Student) RETURN u ORDER BY u.lastname " + req.query.sort;
    } else {
        query = "MATCH (u:Student) RETURN u";
    }
    db.query(query, function(err, student) {
        if (err) throw err;
        res.send(student);
        console.log("Students list sorted and fetched");
    });
    
});

app.get('/students', function(req, res) {
    console.log("zwykly get");

    var query = "MATCH (u:Student) RETURN u";
    db.query(query, function(err, student) {
        if (err) throw err;
        res.send(student);
        console.log("Students list fetched");
    });
});



app.get('/user/create/:name/:login/:password', function(req, res) {
    var name = req.params.name;
    var login = req.params.login;
    var password = req.params.password;

    const requestUser = {
        login: login,
        password: password
    };

    db.save({
        name: name,
        login: login,
        password: password
    }, 'User', function(err, user) {
        res.send(user);
    });

});


app.get('/student/:album', function(req, res) {
    var album = req.params.album;

    var query = [
        "MATCH (u) WHERE u.album=" + album + "",
        "RETURN u"
    ].join(' ');
    db.query(query, function(err, student) {
        res.send(student);
        console.log("Student " + album + " fetched");
    });
});


app.post('/student', function(req, res) {
    var firstname = req.body.firstname;
    var lastname = req.body.lastname;
    var album = req.body.album;
    var note = req.body.note;

    db.save({
        firstname: firstname,
        lastname: lastname,
        album: album,
        note: note
    }, 'Student', function(err, student) {
        res.send(student);
        console.log("Student " + album + " added");
    });
});


app.put('/student/:album', function(req, res) {
    var album = req.params.album;

    var firstname = req.body.firstname;
    var lastname = req.body.lastname;
    var note = req.body.note;

    var query = [
        "MATCH (u:Student) WHERE u.album=" + album + "",
        "RETURN u"
    ].join(' ');

    db.query(query, function(err, students) {
        var student = students[0];
        if (firstname && firstname != student.firstname) {
            student.firstname = firstname;
        }
        if (lastname && lastname != student.lastname) {
            student.lastname = lastname;
        }
        if (note && note != student.note) {
            student.note = note;
        }
        console.log(student);
        db.save(student, function(err, student) {
            res.send(student);
            console.log("Student " + album + " updated");
        });
    });
});


app.delete('/student/:album', function(req, res) {
    var album = req.params.album;

    var query = [
        "MATCH (u) WHERE u.album=" + album + "",
        "DELETE u"
    ].join(' ');
    db.query(query, function(err, student) {
        res.send(student);
        console.log("Student " + album + " deleted");
    });
    
    // db.query(query);

    // db.delete(album, [true], function(err, student) {
    //     res.send(student);
    // });
});

/////////////////////


app.get('/vector/:id', function(req, res) {
    var id = req.params.id;

    db.read(id, function(err, vector) {
        res.send(vector);
    });
});


app.get('/vectors/:ownerId', function(req, res) {
    var ownerId = req.params.ownerId;
    var query = [
        "MATCH (u)-[r:HAS_V]->(v) WHERE id(u)=" + ownerId + "",
        "RETURN v",
        "ORDER BY v.name"
    ].join(' ');
    db.query(query, function(err, vectors) {
        if (err) throw err;
        vectors.forEach(vector => {
            vector.values = JSON.parse("[" + vector.values + "]");
        })
        res.send(vectors);
    });
});


// app.get('/matrices/:ownerId', function(req, res) {
//     var ownerId = req.params.ownerId;
//     var query = [
//         "MATCH (u)-[r:HAS_M]->(m) WHERE id(u)=" + ownerId + "",
//         "RETURN m",
//         "ORDER BY m.name"
//     ].join(' ');
//     db.query(query, function(err, matrices) {
//         if (err) throw err;
//         matrices.forEach(matrix => {
//           matrix.values = oneDimToTwo(matrix.values, matrix.dimension2);
//         });
//         res.send(matrices);
//     });
// });
//
// function oneDimToTwo(array, dim2){
//   console.log(dim2);
//   console.log(array.length);
//   array = JSON.parse("[" + array + "]")
//   var matrix = [];
//   var i, j, temp;
//   for(i=0, j=array.length; i<j; i+=dim2){
//     temp = array.slice(i, i+dim2);
//     console.log(temp);
//     matrix.push(temp);
//   }
//   console.log(matrix);
//   return matrix;
// }


app.post('/vector', function(req, res) {
    var name = req.body.name;
    var ownerId = req.body.ownerId;
    var values = req.body.values;
    var constantName = req.body.constantName;
    var desc = req.body.desc;
    var type = req.body.type;

    db.read(ownerId, function(err, nodeOwner) {
        if (err) throw err;
        db.save({
            name: name,
            ownerId: ownerId,
            values: values,
            constantName: constantName,
            desc: desc,
            type: type
        }, 'Vector', function(err, nodeVector) {
            if (err) throw err;
            db.relate(nodeOwner, 'HAS_V', nodeVector, function(err, rel) {
                res.send(nodeVector);
            });
        });
    });
});


app.put('/vector/:id', function(req, res) {
    var id = req.params.id;
    var name = req.body.name;
    var values = req.body.values;
    var constantName = req.body.constantName;
    var desc = req.body.desc;
    var type = req.body.type;

    db.read(id, function(err, vector) {
        if (name && name != vector.name) {
            vector.name = name;
        }
        if (values && values != vector.values) {
            vector.values = values;
        }
        if (constantName && constantName != vector.constantName) {
            vector.constantName = constantName;
        }
        if (desc && desc != vector.desc) {
            vector.desc = desc;
        }
        if (type && desc != vector.type) {
            vector.type = type;
        }

        db.save(vector, function(err, vector) {
            res.send(vector);
        });
    });
});


app.delete('/vector/:id', function(req, res) {
    var id = req.params.id;

    db.delete(id, [true], function(err, vector) {
        res.send(vector);
    });
});



/////////////////////


app.get('/matrix/:id', function(req, res) {
    var id = req.params.id;

    db.read(id, function(err, matrix) {
        res.send(matrix);
    });
});


app.get('/matrices/:ownerId', function(req, res) {
    var ownerId = req.params.ownerId;
    var query = [
        "MATCH (u)-[r:HAS_M]->(m) WHERE id(u)=" + ownerId + "",
        "RETURN m",
        "ORDER BY m.name"
    ].join(' ');
    db.query(query, function(err, matrices) {
        if (err) throw err;
        matrices.forEach(matrix => {
            matrix.values = oneDimToTwo(matrix.values, matrix.dimension2);
        });
        res.send(matrices);
    });
});

function oneDimToTwo(array, dim2) {
    console.log(dim2);
    console.log(array.length);
    array = JSON.parse("[" + array + "]")
    var matrix = [];
    var i, j, temp;
    for (i = 0, j = array.length; i < j; i += dim2) {
        temp = array.slice(i, i + dim2);
        console.log(temp);
        matrix.push(temp);
    }
    console.log(matrix);
    return matrix;
}

app.post('/matrix', function(req, res) {
    var name = req.body.name;
    var ownerId = req.body.ownerId;
    var values = req.body.values;
    var dimension = req.body.dimension;
    var constantName = req.body.constantName;
    var desc = req.body.desc;
    var type = req.body.type;
    db.read(ownerId, function(err, nodeOwner) {
        if (err) throw err;
        db.save({
            name: name,
            ownerId: ownerId,
            values: values,
            constantName: constantName,
            desc: desc,
            type: type
        }, 'Matrix', function(err, nodeMatrix) {
            if (err) throw err;
            db.relate(nodeOwner, 'HAS_M', nodeMatrix, function(err, rel) {
                res.send(nodeMatrix);
            });
        });
    });
});


app.put('/matrix/:id', function(req, res) {
    var id = req.params.id;
    var name = req.body.name;
    var values = req.body.values;
    var dimension = req.body.dimension;
    var constantName = req.body.constantName;
    var desc = req.body.desc;
    var type = req.body.type;

    db.read(id, function(err, node) {
        if (name && name != node.name) {
            node.name = name;
        }
        if (values && values != node.values) {
            node.values = values;
        }
        if (dimension && dimension != node.dimension) {
            node.dimension = dimension;
        }
        if (constantName && constantName != vector.constantName) {
            vector.constantName = constantName;
        }
        if (desc && desc != vector.desc) {
            vector.desc = desc;
        }
        if (type && desc != vector.type) {
            vector.type = type;
        }

        db.save(node, function(err, matrix) {
            res.send(matrix);
        });
    });
});

app.put('/matrices/:userId', function(req, res) {
    console.log('############PUT');
    // delete all user matrixes
    var name, ownerId, values, dimension, constantName, desc, type;

    var matrices = req.body;
    var userId = req.params.userId;
    var cypher = "START u = node({userId}) " +
        "MATCH (u) -[r:HAS_M]-> (m) " +
        "DELETE r, m " +
        "RETURN u";

    // USUN STARE MACIERZE
    db.query(cypher, {
        userId: Number(userId)
    }, function(err, result) {
        if (err) console.log(err);

        matrices.forEach((matrix) => {
            var id = userId;
            var name = matrix.name;
            var values = matrix.values;
            console.log(values);
            var dimension1 = matrix.dimension1;
            var dimension2 = matrix.dimension2;
            var constantName = matrix.constantName;
            var type = matrix.type;

            // ZAPISZ NOWE
            db.read(userId, function(err, nodeOwner) {
                if (err) throw err;
                db.save({
                    name: name,
                    ownerId: ownerId,
                    constantName: constantName,
                    type: type,
                    values: values,
                    dimension1: dimension1,
                    dimension2: dimension2
                }, 'Matrix', function(err, nodeMatrix) {
                    if (err) throw err;
                    db.relate(nodeOwner, 'HAS_M', nodeMatrix, function(err, rel) {
                        //res.send(nodeMatrix);
                    });
                });
            });
        });
        res.send("dodano macierze");
    });
});

app.put('/vectors/:userId', function(req, res) {
    // delete all user vectors
    var name, ownerId, values, dimension, constantName, desc, type;

    var vectors = req.body;
    var id = req.params.userId;
    var idk = id;
    console.log(idk);
    var cypher = "START u = node({id}) " +
        "MATCH (u) -[r:HAS_V]-> (m) " +
        "DELETE r, m " +
        "RETURN u";
    var params = {};
    params.id = idk;
    // USUN STARE MACIERZE
    db.query(cypher, {
        "id": Number(idk)
    }, function(err, result) {
        if (err) console.log(err);

        vectors.forEach((vector) => {
            // var id = vector.id;
            var name = vector.name;
            var values = vector.values;
            console.log(values);
            var dimension = vector.dimension;
            var constantName = vector.constantName;
            var type = vector.type;

            // ZAPISZ NOWE
            db.read(id, function(err, nodeOwner) {
                if (err) throw err;
                db.save({
                    name: name,
                    ownerId: ownerId,
                    constantName: constantName,
                    type: type,
                    values: values,
                    dimension: dimension
                }, 'Vector', function(err, nodeVector) {
                    if (err) throw err;
                    db.relate(nodeOwner, 'HAS_V', nodeVector, function(err, rel) {
                        //res.send(nodeMatrix);
                    });
                });
            });
        });
        res.send("dodano wektory");
    });
});


// function twoDimToOne(array) {
//    matrix = [].concat.apply([], array);
//    matrix.forEach((value) => ""+value);
//    console.log(matrix);
//    return matrix;
// }

app.delete('/matrix/:id', function(req, res) {
    var id = req.params.id;

    db.delete(id, [true], function(err, matrix) {
        res.send(matrix);
    });
});