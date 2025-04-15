<?php
if (!isset($_GET['id']) || empty($_GET['id'])) {
    die(json_encode(["error" => "Missing balance ID", "status" => "fail"]));
}

$bl_id = intval($_GET['id']);


if (!$stmt = $conn->prepare("DELETE FROM Balansen WHERE bl_id = ?")) {
    die(json_encode(["error" => "Prepared Statement failed on prepare", "errNo" => $conn->errno, "mysqlError" => $conn->error, "status" => "fail"]));
}

if (!$stmt->bind_param("i", $bl_id)) {
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