package com.authentication.jwt.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteBatch;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/students")
public class FirebaseController {

    @GetMapping("/{id}")
    public Map<String, Object> getStudent(@PathVariable String id)
            throws ExecutionException, InterruptedException {

        Firestore db = FirestoreClient.getFirestore();

        DocumentSnapshot doc = db.collection("mca2025")
                .document(id)
                .get()
                .get();

        if (doc.exists()) {
            return doc.getData();
        } else {
            return Map.of("message", "Student not found");
        }
    }

    @PostMapping("/bulk")
    public String bulkInsert() throws Exception {

       ClassPathResource resource = new ClassPathResource("MCA_2025.json");

        InputStream inputStream = resource.getInputStream();

        ObjectMapper mapper = new ObjectMapper();

        List<Map<String, Object>> students =
                mapper.readValue(
                        inputStream,
                        new TypeReference<List<Map<String, Object>>>() {}
                );
                
        Firestore db = FirestoreClient.getFirestore();

        WriteBatch batch = db.batch();

        for (Map<String, Object> student : students) {

            String id = student.get("id").toString();

            batch.set(
                    db.collection("mca2025")
                            .document(id),
                    student
            );
        }

        batch.commit().get();

        return students.size() + " records inserted";
    }
}