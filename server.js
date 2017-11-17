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



app.get('/student/:album', function(req, res) {
    var album = req.params.album;

    var query = [
        "MATCH (u) WHERE u.album='" + album + "'",
        "RETURN u"
    ].join(' ');
    db.query(query, function(err, student) {
        res.send(student[0]);
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
        "MATCH (u:Student) WHERE u.album='" + album + "'",
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
        db.save(student, function(err, student) {
            res.send(student);
            console.log("Student " + album + " updated");
        });
    });
});


app.delete('/student/:album', function(req, res) {
    var album = req.params.album;

    var query = [
        "MATCH (u) WHERE u.album='" + album + "'",
        "DELETE u"
    ].join(' ');
    db.query(query, function(err, student) {
        res.send(student[0]);
        console.log("Student " + album + " deleted");
    });

});