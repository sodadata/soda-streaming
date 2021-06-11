from flask import request, Flask, abort
import json
from queue import Queue
import threading
from typing import Dict
import logging

logger = logging.getLogger(__name__)


class RestConfigurator(threading.Thread):
    def __init__(self, all_queues: Dict[str, Queue]):
        threading.Thread.__init__(self, name="thread-RestConfigurator")
        self.all_queues = all_queues
        self.app = self.define_rest_app()

    def define_rest_app(self):
        app = Flask(__name__)

        @app.route('/api/v0.1/actions', methods=['POST'])
        def create_task():
            if request.json and 'action_request' in request.json: #todo just check total validity
                topic = request.json["topic_description"]
                action = request.json["action_request"]
                self.all_queues[topic].put(action)
                logger.debug(f"Put {action} to {topic} data generator")
                # if not all_queues["specific_queue"].empty(): #todo cleanup
                #     print(all_queues["specific_queue"].get())
            else:
                print(request)
                print(request.json)
                abort(400)
            return json.dumps({'task': "OK"}), 201

        return app

    def run(self):
        self.app.run(host='0.0.0.0')