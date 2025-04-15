<?php
if (!isset($_GET['id']) || empty($_GET['id'])) {
    die(json_encode(["error" => "Missing date ID", "status" => "fail"]));
}

$dt_id = intval($_GET['id']);

if (!$stmt = $conn->prepare("DELETE FROM Datums WHERE dt_id = ?")) {
    die(json_encode(["error" => "Prepared Statement failed on prepare", "errNo" => $conn->errno, "mysqlError" => $conn->error, "status" => "fail"]));
}

if (!$stmt->bind_param("i", $dt_id)) {
    die(json_encode(["error" => "Prepared Statement bind failed", "errNo" => $conn->errno, "mysqlError" => $conn->error, "status" => "fail"]));
}

$stmt->execute();

if ($conn->affected_rows == 0) {
    $stmt->close();
    die(json_encode(["error" => "No rows affected", "errNo" => $conn->errno, "mysqlError" => $conn->error, "status" => "fail"]));
}

$stmt->close();
die(json_encode(["data" => "ok", "message" => "Record deleted successfully", "status" => 200]));
?>